package com.rxlearning.ui.screen.splash

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.rxlearning.ui.base.BaseViewModel
import com.rxlearning.ui.screen.splash.SplashActivity.Companion.DELAY_SPLASH_SECONDS
import com.rxlearning.utils.RxUtils
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashViewModel(application: Application) : BaseViewModel(application) {
    val splashLiveData = MutableLiveData<ScreenType>()

    private val onCheckSuccessConsumer = Consumer<ScreenType> {
        splashLiveData.value = it
    }

    private val onCheckErrorConsumer = Consumer<Throwable> {
        splashLiveData.value = ScreenType.AUTH
    }

    fun checkCurrentSession() {
        Flowable.fromCallable {
            //TODO
//            RxLearningApp.instance.getCurrentUser()?.let { ScreenType.MAIN } ?: ScreenType.AUTH
//            ScreenType.MAIN
            ScreenType.AUTH
        }
                .zipWith(Flowable.timer(DELAY_SPLASH_SECONDS, TimeUnit.SECONDS, Schedulers.io()),
                        BiFunction<ScreenType, Long, ScreenType> { type: ScreenType, _: Long -> type })
                .compose(RxUtils.ioToMainTransformer())
                .subscribe(onCheckSuccessConsumer, onCheckErrorConsumer)
    }
}