package com.rxlearning.ui.screen.main

import android.content.Context
import android.os.Bundle
import com.rxlearning.R
import com.rxlearning.ui.base.BaseLifecycleActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class MainActivity : BaseLifecycleActivity<MainViewModel>() {
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
        //TODO
    }
}
