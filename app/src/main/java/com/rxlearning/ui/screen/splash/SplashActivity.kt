package com.rxlearning.ui.screen.splash

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import com.cleveroad.bootstrap.kotlin_core.utils.misc.isConnected
import com.rxlearning.R
import com.rxlearning.ui.base.BaseLifecycleActivity
import com.rxlearning.ui.base.NoNetworkException
import com.rxlearning.ui.screen.auth.AuthActivity
import com.rxlearning.ui.screen.main.MainActivity

class SplashActivity : BaseLifecycleActivity<SplashViewModel>() {
    override val viewModelClass = SplashViewModel::class.java
    override val containerId = R.id.container
    override val layoutId = R.layout.activity_splash

    companion object {
        const val DELAY_SPLASH_SECONDS = 2L
    }

    private val splashObserver = Observer<ScreenType> {
        it?.let {
            when (it) {
                ScreenType.MAIN -> if (isConnected()) showMainScreen() else onError(NoNetworkException(), true)
                ScreenType.AUTH -> showAuthScreen()
            }
        }
    }

    override fun observeLiveData() {
        with(viewModel) {
            splashLiveData.observe(this@SplashActivity, splashObserver)
            setLoadingLiveData(splashLiveData)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) viewModel.checkCurrentSession()
    }

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent) = showMainScreen()

    private fun showAuthScreen() {
        AuthActivity.start(this)
        finish()
    }

    private fun showMainScreen() {
        MainActivity.start(this)
        finish()
    }

}