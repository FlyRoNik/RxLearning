package com.rxlearning.ui.screen.auth.password

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_validators.MatchValidator
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.rxlearning.RxLearningApp
import com.rxlearning.models.user.RoleType
import com.rxlearning.models.user.SignUp
import com.rxlearning.models.user.UserModel
import com.rxlearning.network.USERS_KEY
import com.rxlearning.network.converters.UserBeanConverterImpl
import com.rxlearning.ui.base.BaseViewModel
import com.rxlearning.utils.matchPasswordValidator
import durdinapps.rxfirebase2.RxFirebaseAuth
import durdinapps.rxfirebase2.RxFirebaseUser
import durdinapps.rxfirebase2.RxFirestore
import io.reactivex.rxkotlin.addTo


class PasswordViewModel(application: Application) : BaseViewModel(application) {
    private val userBeanConverter = UserBeanConverterImpl()
    private val matchPasswordValidator: MatchValidator by lazy { matchPasswordValidator(application) }

    val submitPasswordLiveData = MutableLiveData<Unit>()

    fun submitPassword(credential: PhoneAuthCredential, signUp: SignUp, password: String, confirmPassword: String) {
        if (isPasswordValid(password, confirmPassword).isValid) {
            isLoadingLiveData.value = true
            RxFirebaseAuth.createUserWithEmailAndPassword(RxLearningApp.instance.auth, signUp.email, password)
                    .flatMap { RxFirebaseUser.linkWithCredential(it.user, credential) }
                    .flatMapCompletable { result ->
                        with(signUp) {
                            userBeanConverter.convertOutToIn(UserModel(result.user.uid, firstName, lastName, email, phone, RoleType.STUDENT))
                        }.let { userBean ->
                            FirebaseFirestore.getInstance()
                                    .collection(USERS_KEY)
                                    .document(result.user.uid).let {
                                        RxFirestore.setDocument(it, userBean)
                                    }
                        }
                    }.subscribe({ submitPasswordLiveData.value = Unit }, onErrorConsumer::accept)
                    .addTo(compositeDisposable)
        }
    }

    private fun isPasswordValid(password: String, confirmPassword: String) =
            matchPasswordValidator.validate(password, confirmPassword).also {
                if (!it.isValid) errorLiveData.value = it.errorMessage
            }
}