package com.rxlearning

import android.app.Application
import android.support.annotation.FontRes
import android.support.annotation.PluralsRes
import android.support.annotation.RawRes
import android.support.annotation.StringRes
import android.support.v4.content.res.ResourcesCompat
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

fun readRawResource(@RawRes rawResource: Int) = RxLearningApp.instance.resources.openRawResource(rawResource).bufferedReader().use { it.readText() }

fun getStringApp(@StringRes stringId: Int) = RxLearningApp.instance.getString(stringId)

fun getStringApp(@StringRes stringId: Int, vararg formatArgs: Any?) =
        RxLearningApp.instance.getString(stringId, *formatArgs)

fun getQuantityStringApp(@PluralsRes stringId: Int, quantity: Int, vararg formatArgs: Any?) =
        RxLearningApp.instance.resources.getQuantityString(stringId, quantity, *formatArgs)

fun getFontApp(@FontRes fontRes: Int) = ResourcesCompat.getFont(RxLearningApp.instance, fontRes)