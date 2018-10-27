package com.rxlearning.ui.screen.auth.confirm

import com.google.firebase.auth.PhoneAuthCredential
import com.rxlearning.models.user.SignUp

interface ConfirmCallback {
    /**
     * Show password screen [com.rxlearning.ui.screen.auth.password.PasswordFragment]
     * with [credential] and [signUp] model
     */
    fun showPasswordScreen(credential: PhoneAuthCredential, signUp: SignUp)
}