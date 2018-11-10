package com.rxlearning.ui.screen.main.navigation.profile

import android.content.Intent

interface ProfileCallback {
    /**
     * Hide navigation bar
     */
    fun hideNavigationBar()

    /**
     * Show navigation bar
     */
    fun showNavigationBar()

    /**
     * Call method [android.app.Activity.startActivityForResult]
     * with [intent] and [requestCode]
     */
    fun onStartActivityForResult(intent: Intent, requestCode: Int)

    /**
     * Show change password screen [com.rxlearning.ui.screen.main.change_password.ChangePasswordFragment]
     */
    fun showChangePasswordScreen()
}