package com.rxlearning.ui.screen.auth.sign_in

interface SignInCallback {
    /**
     * Show main screen [com.rxlearning.ui.screen.main.MainActivity]
     */
    fun showMainScreen()

    /**
     * Show forgot password screen [com.rxlearning.ui.screen.auth.reset_password.ResetPasswordFragment]
     */
    fun showForgotPassword()
}