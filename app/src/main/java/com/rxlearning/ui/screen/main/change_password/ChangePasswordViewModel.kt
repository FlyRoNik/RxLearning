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
import durdinapps.rxfirebase2.RxFirebaseUser
import io.reactivex.rxkotlin.addTo

class ChangePasswordViewModel(application: Application) : BaseViewModel(application) {
    private val matchPasswordValidator: MatchValidator by lazy { matchPasswordValidator(application) }
    private val passwordValidator: Validator by lazy { passwordValidator(application) }

    val changePasswordLiveData = MutableLiveData<Unit>()

    fun changePassword(password: String, newPassword: String, confirmPassword: String) {
        val currentUser = RxLearningApp.instance.getCurrentUser()
        if (isPasswordValid(password).isValid && isPasswordValid(newPassword, confirmPassword).isValid && currentUser != null) {
            isLoadingLiveData.value = true
            RxFirebaseUser.reAuthenticate(currentUser, EmailAuthProvider.getCredential(currentUser.email
                    ?: "", password))
                    .andThen(RxFirebaseUser.updatePassword(currentUser, newPassword))
                    .subscribe({
                        changePasswordLiveData.value = Unit
                    }, onErrorConsumer::accept)
                    .addTo(compositeDisposable)
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