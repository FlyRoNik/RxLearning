package com.rxlearning.ui.screen.auth.reset_password

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_validators.Validator
import com.rxlearning.RxLearningApp
import com.rxlearning.ui.base.BaseViewModel
import com.rxlearning.utils.emailValidator
import durdinapps.rxfirebase2.RxFirebaseAuth
import io.reactivex.rxkotlin.addTo

class ResetPasswordViewModel(application: Application) : BaseViewModel(application) {
    private val emailValidator: Validator by lazy { emailValidator(application) }

    val resetPasswordLiveData = MutableLiveData<Unit>()

    fun resetPassword(email: String) {
        if (isEmailValid(email).isValid) {
            isLoadingLiveData.value = true
            RxFirebaseAuth.sendPasswordResetEmail(RxLearningApp.instance.auth, email)
                    .subscribe({ resetPasswordLiveData.value = Unit }, onErrorConsumer::accept)
                    .addTo(compositeDisposable)
        }
    }

    private fun isEmailValid(email: String?) = emailValidator.validate(email).also {
        if (!it.isValid) errorLiveData.value = it.errorMessage
    }

}