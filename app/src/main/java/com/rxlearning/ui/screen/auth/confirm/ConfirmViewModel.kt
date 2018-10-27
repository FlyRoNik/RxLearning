package com.rxlearning.ui.screen.auth.confirm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.rxlearning.TIMEOUT_IN_SECONDS
import com.rxlearning.models.user.SignUp
import com.rxlearning.ui.base.BaseViewModel
import java.util.concurrent.TimeUnit

class ConfirmViewModel(application: Application) : BaseViewModel(application) {
    val confirmLiveData = MutableLiveData<PhoneAuthCredential>()
    val resendLiveData = MutableLiveData<String>()

    fun confirm(token: String, smsCode: String) {
        isLoadingLiveData.value = true
        confirmLiveData.value = PhoneAuthProvider.getCredential(token, smsCode)
    }

    fun resend(signUp: SignUp) {
        isLoadingLiveData.value = true
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                signUp.phone,
                TIMEOUT_IN_SECONDS,
                TimeUnit.SECONDS,
                { it.run() },
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        //do nothing
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        onErrorConsumer.accept(e)
                    }

                    override fun onCodeSent(token: String, p1: PhoneAuthProvider.ForceResendingToken?) {
                        resendLiveData.postValue(token)
                    }

                })
    }

}