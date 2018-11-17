package com.rxlearning.ui.screen.main.change_password

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cleveroad.bootstrap.kotlin_core.utils.misc.hideKeyboard
import com.rxlearning.KEYBOARD_HIDE_DELAY_MILLISECONDS
import com.rxlearning.R
import com.rxlearning.extensions.getStringText
import com.rxlearning.extensions.setClickListeners
import com.rxlearning.extensions.switchPassword
import com.rxlearning.ui.base.BaseLifecycleFragment
import com.rxlearning.ui.base.RequestCodes
import com.rxlearning.ui.base.dialog.MessageDialogFragment
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_change_password.*
import java.util.concurrent.TimeUnit

class ChangePasswordFragment : BaseLifecycleFragment<ChangePasswordViewModel>(), View.OnClickListener {
    override val viewModelClass = ChangePasswordViewModel::class.java
    override val layoutId = R.layout.fragment_change_password

    companion object {
        fun newInstance() = ChangePasswordFragment().apply {
            arguments = Bundle()
        }
    }

    override fun observeLiveData() {
        with(viewModel) {
            changePasswordLiveData.observe(this@ChangePasswordFragment, changePasswordLiveDataObserver)
            setLoadingLiveData(changePasswordLiveData)
        }
    }

    override fun getScreenTitle() = NO_TITLE
    override fun hasToolbar() = true
    override fun getToolbarId() = R.id.toolbar
    override fun hasResultFromDialog() = true
    override fun needToShowBackNav() = true

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent) =
            if (requestCode == RequestCodes.RequestCode.REQUEST_DIALOG_CHANGE_PASSWORD()) backPressed() else Unit

    private val changePasswordLiveDataObserver = Observer<Unit> {
        MessageDialogFragment.newInstance(getString(R.string.title_dialog_change_password), getString(R.string.subtitle_dialog_change_password))
                .showForResult(this, RequestCodes.RequestCode.REQUEST_DIALOG_CHANGE_PASSWORD())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(ivTriggerPasswordVisible, ivTriggerNewPasswordVisible, ivTriggerConfirmPasswordVisible, tvSave)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivTriggerPasswordVisible -> etPassword.switchPassword { ivTriggerPasswordVisible.switchEye(it) }
            R.id.ivTriggerNewPasswordVisible -> etNewPassword.switchPassword { ivTriggerNewPasswordVisible.switchEye(it) }
            R.id.ivTriggerConfirmPasswordVisible -> etConfirmPassword.switchPassword { ivTriggerConfirmPasswordVisible.switchEye(it) }
            R.id.tvSave -> viewModel.changePassword(etPassword.getStringText(), etNewPassword.getStringText(), etConfirmPassword.getStringText())
        }
    }

    override fun handleNavigation() {
        hideKeyboard()
        Flowable.timer(KEYBOARD_HIDE_DELAY_MILLISECONDS, TimeUnit.MILLISECONDS, Schedulers.io()).subscribe({ backPressed() }, { onError(it) })
    }

}