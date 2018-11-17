package com.rxlearning.ui.screen.main.change_password

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_validators.MatchValidator
import com.cleveroad.bootstrap.kotlin_validators.Validator
import com.google.firebase.auth.EmailAuthProvider
import com.rxlearning.RxLearningApp
import com.rxlearning.ui.base.BaseViewModel
import com.rxlearning.utils.matchPasswordValidator
import com.rxlearning.utils.passwordValidator

class ChangePasswordViewModel(application: Application) : BaseViewModel(application) {
    private val matchPasswordValidator: MatchValidator by lazy { matchPasswordValidator(application) }
    private val passwordValidator: Validator by lazy { passwordValidator(application) }

    val changePasswordLiveData = MutableLiveData<Unit>()

    fun changePassword(password: String, newPassword: String, confirmPassword: String) {
        if (isPasswordValid(password).isValid && isPasswordValid(newPassword, confirmPassword).isValid) {
            isLoadingLiveData.value = true
            RxLearningApp.instance.getCurrentUser()?.let { currentUser ->
                currentUser.reauthenticate(EmailAuthProvider.getCredential(currentUser.email
                        ?: "", password))
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                currentUser.updatePassword(newPassword).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        changePasswordLiveData.postValue(Unit)
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
    }

    private fun isPasswordValid(password: String, confirmPassword: String) =
            matchPasswordValidator.validate(password, confirmPassword).also {
                if (!it.isValid) errorLiveData.value = it.errorMessage
            }

    private fun isPasswordValid(password: String?) = passwordValidator.validate(password).also {
        if (!it.isValid) errorLiveData.value = it.errorMessage
    }
}