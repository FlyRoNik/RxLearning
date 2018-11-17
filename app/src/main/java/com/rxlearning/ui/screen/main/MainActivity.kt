package com.rxlearning.ui.screen.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.rxlearning.R
import com.rxlearning.ui.base.BaseLifecycleActivity
import com.rxlearning.ui.screen.info.InfoFragment
import com.rxlearning.ui.screen.info.TypeInfo
import com.rxlearning.ui.screen.main.change_password.ChangePasswordFragment
import com.rxlearning.ui.screen.main.navigation.NavigationCallback
import com.rxlearning.ui.screen.main.navigation.NavigationFragment
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.toast

class MainActivity : BaseLifecycleActivity<MainViewModel>(), NavigationCallback {
    override val viewModelClass = MainViewModel::class.java
    override val containerId = R.id.container
    override val layoutId = R.layout.activity_main

    companion object {
        fun start(context: Context) {
            with(context) {
                intentFor<MainActivity>()
                        .newTask()
                        .clearTask()
                        .let { startActivity(it) }
            }
        }
    }

    override fun observeLiveData() {
        //Do nothing
    }

    override fun hasProgressBar() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        replaceFragment(NavigationFragment.newInstance(), false)
    }

    override fun onStartActivityForResult(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    override fun showChangePasswordScreen() {
        replaceFragment(ChangePasswordFragment.newInstance())
    }

    override fun showPrivacyPolicyScreen() {
        replaceFragment(InfoFragment.newInstance(TypeInfo.PRIVACY_POLICY))
    }

    override fun showTermsOfUseScreen() {
        replaceFragment(InfoFragment.newInstance(TypeInfo.TERMS_OF_USE))
    }

    override fun showFeedbackScreen() {
        toast("Not implemented")
        //TODO need implement
//        replaceFragment(FeedbackFragment.newInstance())
    }


}
