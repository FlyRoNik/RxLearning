package com.rxlearning.ui.screen.auth.password

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import com.cleveroad.bootstrap.kotlin_core.utils.misc.hideKeyboard
import com.google.firebase.auth.PhoneAuthCredential
import com.rxlearning.KEYBOARD_HIDE_DELAY_MILLISECONDS
import com.rxlearning.R
import com.rxlearning.extensions.getStringText
import com.rxlearning.extensions.setClickListeners
import com.rxlearning.extensions.switchPassword
import com.rxlearning.models.user.SignUp
import com.rxlearning.ui.base.BaseLifecycleFragment
import com.rxlearning.ui.base.FragmentArgumentDelegate
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_password.*
import org.jetbrains.anko.support.v4.act
import java.util.concurrent.TimeUnit

class PasswordFragment : BaseLifecycleFragment<PasswordViewModel>(), View.OnClickListener {
    override val viewModelClass = PasswordViewModel::class.java
    override val layoutId = R.layout.fragment_password

    companion object {
        fun newInstance(credential: PhoneAuthCredential, signUp: SignUp) = PasswordFragment().apply {
            this.credential = credential
            this.signUp = signUp
        }
    }

    override fun getScreenTitle() = NO_TITLE
    override fun hasToolbar() = true
    override fun getToolbarId() = R.id.toolbar
    override fun needKeyboardListener() = true

    private var credential by FragmentArgumentDelegate<PhoneAuthCredential>()
    private var signUp by FragmentArgumentDelegate<SignUp>()

    override fun observeLiveData() {
        with(viewModel) {
            submitPasswordLiveData.observe(this@PasswordFragment, signInLiveDataObserver)
            setLoadingLiveData(submitPasswordLiveData)
        }
    }

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

    private val signInLiveDataObserver = Observer<Unit> { passwordCallback?.showMainScreen() }
    private var passwordCallback: PasswordCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        passwordCallback = bindInterfaceOrThrow<PasswordCallback>(parentFragment, context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(ivTriggerPasswordVisible, ivTriggerConfirmPasswordVisible, tvSubmit)
    }

    override fun onDetach() {
        passwordCallback = null
        super.onDetach()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivTriggerPasswordVisible -> etPassword.switchPassword { ivTriggerPasswordVisible.switchEye(it) }
            R.id.ivTriggerConfirmPasswordVisible -> etConfirmPassword.switchPassword { ivTriggerConfirmPasswordVisible.switchEye(it) }
            R.id.tvSubmit ->
                viewModel.submitPassword(credential, signUp, etPassword.getStringText(), etConfirmPassword.getStringText())

        }
    }

    override fun handleNavigation() {
        hideKeyboard()
        Flowable.timer(KEYBOARD_HIDE_DELAY_MILLISECONDS, TimeUnit.MILLISECONDS, Schedulers.io()).subscribe({ backPressed() }, { onError(it) })
    }

}
