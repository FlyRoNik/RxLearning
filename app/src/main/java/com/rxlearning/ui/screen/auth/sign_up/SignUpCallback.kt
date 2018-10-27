package com.rxlearning.ui.screen.auth.sign_up

import com.rxlearning.models.user.SignUp

interface SignUpCallback {
    /**
     * Show confirm screen [com.rxlearning.ui.screen.auth.confirm.ConfirmFragment]
     * with temporary [token] and [signUp] model
     */
    fun showConfirmScreen(token: String, signUp: SignUp)

    /**
     * Show terms of use screen [com.rxlearning.ui.screen.info.InfoFragment]
     */
    fun showTermsOfUseScreen()
}