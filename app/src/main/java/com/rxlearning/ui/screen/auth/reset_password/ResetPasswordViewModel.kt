package com.rxlearning.ui.screen.auth.reset_password

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_validators.Validator
import com.rxlearning.RxLearningApp
import com.rxlearning.ui.base.BaseViewModel
import com.rxlearning.utils.emailValidator

class ResetPasswordViewModel(application: Application) : BaseViewModel(application) {
    private val emailValidator: Validator by lazy { emailValidator(application) }

    val resetPasswordLiveData = MutableLiveData<Unit>()

    fun resetPassword(email: String) {
        if (isEmailValid(email).isValid) {
            isLoadingLiveData.value = true
            RxLearningApp.instance.auth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    resetPasswordLiveData.postValue(Unit)
                } else {
                    onErrorConsumer.accept(it.exception)
                }
            }
        }
    }

    private fun isEmailValid(email: String?) = emailValidator.validate(email).also {
        if (!it.isValid) errorLiveData.value = it.errorMessage
    }

}