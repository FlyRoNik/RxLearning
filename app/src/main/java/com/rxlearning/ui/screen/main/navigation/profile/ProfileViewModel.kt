package com.rxlearning.ui.screen.main.navigation.profile

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_validators.PhoneValidator
import com.cleveroad.bootstrap.kotlin_validators.Validator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.rxlearning.RxLearningApp
import com.rxlearning.extensions.removeSpaces
import com.rxlearning.models.user.User
import com.rxlearning.network.USERS_KEY
import com.rxlearning.network.bean.UserBean
import com.rxlearning.network.converters.UserBeanConverterImpl
import com.rxlearning.ui.base.BaseViewModel
import com.rxlearning.utils.emailValidator
import com.rxlearning.utils.nameValidator
import com.rxlearning.utils.phoneValidator
import durdinapps.rxfirebase2.RxFirestore
import io.reactivex.rxkotlin.addTo
import java.util.*

class ProfileViewModel(application: Application) : BaseViewModel(application) {
    private val userBeanConverter = UserBeanConverterImpl()
    private val emailValidator: Validator by lazy { emailValidator(application) }
    private val nameValidator: Validator by lazy { nameValidator(application) }
    private val phoneValidator: PhoneValidator by lazy { phoneValidator(application) }
    private val phoneNumberUtil = PhoneNumberUtil.getInstance()

    private var user: User? = null

    val loadUserLiveData = MutableLiveData<User>()
    val editUserLiveData = MutableLiveData<User>()
    val editAvatarLiveData = MutableLiveData<String>()
    val logoutLiveData = MutableLiveData<Unit>()

    fun loadUser() {
        user?.let {
            loadUserLiveData.value = it
        } ?: RxLearningApp.instance.getCurrentUser()?.let {
            isLoadingLiveData.value = true
            val reference = FirebaseFirestore.getInstance()
                    .collection(USERS_KEY)
                    .document(it.uid)
            RxFirestore.getDocument(reference)
                    .subscribe({ doc ->
                        doc.toObject(UserBean::class.java)?.let { bean ->
                            user = userBeanConverter.convertInToOut(bean)
                            loadUserLiveData.postValue(user)
                        }
                    }, onErrorConsumer::accept)
                    .addTo(compositeDisposable)
        }
    }

    fun editUser(firstName: String, lastName: String, email: String, phone: String) {
        if (isNameValid(firstName).isValid && isNameValid(lastName).isValid && isEmailValid(email).isValid && isPhoneValid(phone).isValid) {
            isLoadingLiveData.value = true
            val phoneNumber = phoneNumberUtil.format(phoneNumberUtil.parse(phone, Locale.getDefault().country),
                    PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL).removeSpaces()
            //TODO edit user profile
        }
    }

    fun editAvatar(avatar: String) {
        isLoadingLiveData.value = true
        //TODO edit avatar
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