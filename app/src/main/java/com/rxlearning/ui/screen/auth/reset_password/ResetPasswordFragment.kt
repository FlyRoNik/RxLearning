package com.rxlearning.ui.screen.auth.reset_password

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import com.cleveroad.bootstrap.kotlin_core.utils.misc.hideKeyboard
import com.rxlearning.KEYBOARD_HIDE_DELAY_MILLISECONDS
import com.rxlearning.R
import com.rxlearning.extensions.getStringText
import com.rxlearning.extensions.setClickListeners
import com.rxlearning.ui.base.BaseLifecycleFragment
import com.rxlearning.ui.base.RequestCodes
import com.rxlearning.ui.base.dialog.MessageDialogFragment
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_reset_password.*
import org.jetbrains.anko.support.v4.act
import java.util.concurrent.TimeUnit

class ResetPasswordFragment : BaseLifecycleFragment<ResetPasswordViewModel>(), View.OnClickListener {
    override val viewModelClass = ResetPasswordViewModel::class.java
    override val layoutId = R.layout.fragment_reset_password

    companion object {
        fun newInstance() = ResetPasswordFragment().apply {
            arguments = Bundle()
        }
    }

    override fun observeLiveData() {
        with(viewModel) {
            resetPasswordLiveData.observe(this@ResetPasswordFragment, resetPasswordLiveDataObserver)
            setLoadingLiveData(resetPasswordLiveData)
        }
    }

    override fun getScreenTitle() = NO_TITLE
    override fun hasToolbar() = true
    override fun getToolbarId() = R.id.toolbar
    override fun needKeyboardListener() = true
    override fun hasResultFromDialog() = true

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent) =
            if (requestCode == RequestCodes.RequestCode.REQUEST_DIALOG_RESET_PASSWORD()) backPressed() else Unit

    override fun onKeyboardSwitch(isShow: Boolean) {
        if (isShow) {
            TypedValue().also {
                if (act.theme.resolveAttribute(android.R.attr.actionBarSize, it, true)) {
                    flToolbarContainer.layoutParams = flToolbarContainer.layoutParams.apply {
                        height = TypedValue.complexToDimensionPixelSize(it.data, resources.displayMetrics)
                    }
                }
            }
        } else {
            flToolbarContainer.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    resources.getDimensionPixelSize(R.dimen.toolbar_with_logo_height))
        }
    }

    private val resetPasswordLiveDataObserver = Observer<Unit> {
        MessageDialogFragment.newInstance(getString(R.string.title_dialog_reset_password), getString(R.string.subtitle_dialog_reset_password))
                .showForResult(this, RequestCodes.RequestCode.REQUEST_DIALOG_RESET_PASSWORD())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(tvSubmit)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvSubmit -> viewModel.resetPassword(etEmail.getStringText())
        }
    }

    override fun handleNavigation() {
        hideKeyboard()
        Flowable.timer(KEYBOARD_HIDE_DELAY_MILLISECONDS, TimeUnit.MILLISECONDS, Schedulers.io()).subscribe({ backPressed() }, { onError(it) })
    }
}