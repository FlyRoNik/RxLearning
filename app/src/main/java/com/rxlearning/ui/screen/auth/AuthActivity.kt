package com.rxlearning.ui.screen.auth

import android.content.Context
import android.os.Bundle
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.rxlearning.R
import com.rxlearning.ui.base.BaseLifecycleActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

class AuthActivity : BaseLifecycleActivity<AuthViewModel>()
//        ,SignInCallback, SignUpCallback, ConfirmCallback, PasswordCallback, CardCallback
{
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

    val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential?) {
            //TODO
            toast(p0?.smsCode ?: "")
        }

        override fun onVerificationFailed(p0: FirebaseException?) {
            //TODO
            toast(p0?.message ?: "")
        }
    }

    override fun observeLiveData() {
        //Do nothing
    }

    override fun hasProgressBar() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        replaceFragment(ChooseFragment.newInstance(), false)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+380671148073",        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                callback)
    }

//    override fun showConfirmScreen(token: String, signUp: SignUp) {
//        replaceFragment(ConfirmFragment.newInstance(token, signUp))
//    }
//
//    override fun showMainScreen() {
//        MainActivity.start(this)
//        finish()
//    }
//
//    override fun showPasswordScreen(token: String) {
//        replaceFragment(PasswordFragment.newInstance(token))
//    }
//
//    override fun showCardScreen(token: String) {
//        replaceFragment(CardFragment.newInstance(token))
//    }
//
//    override fun showForgotPassword() {
//        replaceFragment(ResetPasswordFragment.newInstance())
//    }
//
//    override fun showTermsOfUseScreen() {
//        replaceFragment(InfoFragment.newInstance(TypeInfo.TERMS_OF_USE))
//    }
}
