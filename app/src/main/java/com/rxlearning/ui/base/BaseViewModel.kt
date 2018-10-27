package com.rxlearning.ui.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val errorLiveData = MutableLiveData<Any>()
    val isLoadingLiveData = MediatorLiveData<Boolean>()
    protected val compositeDisposable = CompositeDisposable()

    fun setLoadingLiveData(vararg mutableLiveData: MutableLiveData<*>) {
        mutableLiveData.forEach { liveData ->
            isLoadingLiveData.apply {
                this.removeSource(liveData)
                this.addSource(liveData) { this.value = false }
            }
        }
    }

    val onErrorConsumer = Consumer<Throwable> {
        errorLiveData.value = it.message
    }

    override fun onCleared() {
        compositeDisposable.takeIf { it.isDisposed }?.let { it.dispose() }
        compositeDisposable.clear()
        super.onCleared()
    }

}