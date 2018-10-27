package com.rxlearning.ui.screen.auth

import android.content.Context
import android.os.Bundle
import com.google.firebase.auth.PhoneAuthCredential
import com.rxlearning.R
import com.rxlearning.models.user.SignUp
import com.rxlearning.ui.base.BaseLifecycleActivity
import com.rxlearning.ui.screen.auth.choose.ChooseFragment
import com.rxlearning.ui.screen.auth.confirm.ConfirmCallback
import com.rxlearning.ui.screen.auth.confirm.ConfirmFragment
import com.rxlearning.ui.screen.auth.password.PasswordCallback
import com.rxlearning.ui.screen.auth.password.PasswordFragment
import com.rxlearning.ui.screen.auth.sign_in.SignInCallback
import com.rxlearning.ui.screen.auth.sign_up.SignUpCallback
import com.rxlearning.ui.screen.info.InfoFragment
import com.rxlearning.ui.screen.info.TypeInfo
import com.rxlearning.ui.screen.main.MainActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class AuthActivity : BaseLifecycleActivity<AuthViewModel>(), SignInCallback,
        SignUpCallback, ConfirmCallback, PasswordCallback {
    override val viewModelClass = AuthViewModel::class.java
    override val containerId = R.id.container
    override val layoutId = R.layout.activity_auth

    companion object {
        fun start(context: Context) {
            with(context) {
                intentFor<AuthActivity>()
                        .newTask()
                        .clearTask()
                        .let { startActivity(it) }
            }
        }
    }

    override fun observeLiveData() {
        //Do nothing
    }

    override fun hasProgressBar() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(ChooseFragment.newInstance(), false)
    }

    //<editor-fold desc="SignInCallback">
    override fun showMainScreen() {
        MainActivity.start(this)
        finish()
    }

    override fun showForgotPassword() {
//        replaceFragment(ResetPasswordFragment.newInstance())
    }
    //</editor-fold>

    //<editor-fold desc="SignUpCallback">
    override fun showConfirmScreen(token: String, signUp: SignUp) {
        replaceFragment(ConfirmFragment.newInstance(token, signUp))
    }

    override fun showTermsOfUseScreen() {
        replaceFragment(InfoFragment.newInstance(TypeInfo.TERMS_OF_USE))
    }
    //</editor-fold>

    override fun showPasswordScreen(credential: PhoneAuthCredential, signUp: SignUp) {
        replaceFragment(PasswordFragment.newInstance(credential, signUp))
    }

}
