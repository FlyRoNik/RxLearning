package com.rxlearning.ui.screen.auth.sign_in

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_validators.Validator
import com.rxlearning.RxLearningApp
import com.rxlearning.ui.base.BaseViewModel
import com.rxlearning.utils.emailValidator
import com.rxlearning.utils.passwordValidator

class SignInViewModel(application: Application) : BaseViewModel(application) {
    private val emailValidator: Validator by lazy { emailValidator(application) }
    private val passwordValidator: Validator by lazy { passwordValidator(application) }

    val signInLiveData = MutableLiveData<Unit>()

    fun signIn(email: String, password: String) {
        if (isEmailValid(email).isValid && isPasswordValid(password).isValid) {
            isLoadingLiveData.value = true
            RxLearningApp.instance.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            signInLiveData.value = Unit
                        } else {
                            onErrorConsumer.accept(it.exception)
                        }
                    }
        }
    }

    private fun isEmailValid(email: String?) = emailValidator.validate(email).also {
        if (!it.isValid) errorLiveData.value = it.errorMessage
    }

    private fun isPasswordValid(password: String?) = passwordValidator.validate(password).also {
        if (!it.isValid) errorLiveData.value = it.errorMessage
    }

}