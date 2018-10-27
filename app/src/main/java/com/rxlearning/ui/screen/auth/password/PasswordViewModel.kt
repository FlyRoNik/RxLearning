package com.rxlearning.ui.screen.auth.password

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_validators.MatchValidator
import com.google.firebase.auth.PhoneAuthCredential
import com.rxlearning.RxLearningApp
import com.rxlearning.models.user.SignUp
import com.rxlearning.ui.base.BaseViewModel
import com.rxlearning.utils.matchPasswordValidator

class PasswordViewModel(application: Application) : BaseViewModel(application) {
    private val matchPasswordValidator: MatchValidator by lazy { matchPasswordValidator(application) }

    val submitPasswordLiveData = MutableLiveData<Unit>()

    fun submitPassword(credential: PhoneAuthCredential, signUp: SignUp, password: String, confirmPassword: String) {
        if (isPasswordValid(password, confirmPassword).isValid) {
            isLoadingLiveData.value = true
            RxLearningApp.instance.auth.createUserWithEmailAndPassword(signUp.email, password)
                    .addOnCompleteListener {
                        val user = it.result?.user
                        if (it.isSuccessful && user != null) {
                            user.linkWithCredential(credential)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            //TODO save full name
                                            submitPasswordLiveData.value = Unit
                                        } else {
                                            onErrorConsumer.accept(it.exception)
                                        }
                                    }
                        } else {
                            onErrorConsumer.accept(it.exception)
                        }
                    }

        }
    }

    private fun isPasswordValid(password: String, confirmPassword: String) =
            matchPasswordValidator.validate(password, confirmPassword).also {
                if (!it.isValid) errorLiveData.value = it.errorMessage
            }
}