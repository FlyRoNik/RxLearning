package com.rxlearning.ui.screen.main.navigation

import android.content.Intent

interface NavigationCallback {
    /**
     * Call method [android.app.Activity.startActivityForResult]
     * with [intent] and [requestCode]
     */
    fun onStartActivityForResult(intent: Intent, requestCode: Int)

    /**
     * Show change password screen [com.rxlearning.ui.screen.main.change_password.ChangePasswordFragment]
     */
    fun showChangePasswordScreen()

    fun showPrivacyPolicyScreen()

    fun showTermsOfUseScreen()

    fun showFeedbackScreen()

}