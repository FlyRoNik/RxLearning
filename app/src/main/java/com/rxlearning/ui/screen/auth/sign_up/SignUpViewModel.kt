package com.rxlearning.ui.screen.auth.sign_up

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_validators.PhoneValidator
import com.cleveroad.bootstrap.kotlin_validators.Validator
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.rxlearning.R
import com.rxlearning.RxLearningApp
import com.rxlearning.TIMEOUT_IN_SECONDS
import com.rxlearning.extensions.removeSpaces
import com.rxlearning.models.user.SignUp
import com.rxlearning.ui.base.BaseViewModel
import com.rxlearning.utils.emailValidator
import com.rxlearning.utils.nameValidator
import com.rxlearning.utils.phoneValidator
import java.util.*
import java.util.concurrent.TimeUnit

class SignUpViewModel(application: Application) : BaseViewModel(application) {
    private val emailValidator: Validator by lazy { emailValidator(application) }
    private val nameValidator: Validator by lazy { nameValidator(application) }
    private val phoneValidator: PhoneValidator by lazy { phoneValidator(application) }
    private val phoneNumberUtil = PhoneNumberUtil.getInstance()

    val signUpLiveData = MutableLiveData<Pair<String, SignUp>>()

    fun signUp(check: Boolean, firstName: String, lastName: String, email: String, phone: String) {
        if (isCheckTermsOfUse(check) && isNameValid(firstName).isValid && isNameValid(lastName).isValid &&
                isEmailValid(email).isValid && isPhoneValid(phone).isValid) {
            isLoadingLiveData.value = true
            val phoneNumber = phoneNumberUtil.format(phoneNumberUtil.parse(phone, Locale.getDefault().country),
                    PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL).removeSpaces()

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,
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
                            signUpLiveData.postValue(token to SignUp(firstName, lastName, email, phoneNumber))
                        }

                    })
        }
    }

    private fun isCheckTermsOfUse(check: Boolean) = check.also {
        if (!it) errorLiveData.value = RxLearningApp.instance.getString(R.string.terms_of_use_not_check)
    }

    private fun isNameValid(name: String?) = nameValidator.validate(name).also {
        if (!it.isValid) errorLiveData.value = it.errorMessage
    }

    private fun isEmailValid(email: String?) = emailValidator.validate(email).also {
        if (!it.isValid) errorLiveData.value = it.errorMessage
    }

    private fun isPhoneValid(phone: String?) = phoneValidator.validate(phone, Locale.getDefault().country).also {
        if (!it.isValid) errorLiveData.value = it.errorMessage
    }
}
