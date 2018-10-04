package com.rxlearning

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

class RxLearningApp : Application() {

    companion object {
        lateinit var instance: RxLearningApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) Fabric.with(this, Crashlytics())
//        DatabaseCreator.createDb(this)
//        Stetho.initializeWithDefaults(this)
    }
}