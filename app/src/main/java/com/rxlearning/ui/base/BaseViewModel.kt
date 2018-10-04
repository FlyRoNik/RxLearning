package com.rxlearning.ui.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.cleveroad.bootstrap.kotlin_core.utils.withNotNull
import com.google.android.gms.common.api.ApiException
import io.reactivex.functions.Consumer

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val EMPTY_ERROR = ""
    }

    val errorLiveData = MutableLiveData<Any>()
    val isLoadingLiveData = MediatorLiveData<Boolean>()

    fun setLoadingLiveData(vararg mutableLiveData: MutableLiveData<*>) {
        mutableLiveData.forEach { liveData ->
            isLoadingLiveData.apply {
                this.removeSource(liveData)
                this.addSource(liveData) { this.value = false }
            }
        }
    }

    val onErrorConsumer = Consumer<Throwable> {
        val errorString = parseApiException(it)
        errorLiveData.value = if (errorString.isNotEmpty()) errorString else it.message
    }

    private fun parseApiException(throwable: Throwable) =
            withNotNull(throwable as? ApiException) {
//                errors?.let {
//                    it.takeIf { it.isNotEmpty() }
//                            ?.let {
//                                StringBuffer().apply {
//                                    for (index in (0 until it.size)) {
//                                        append(it[index].message).append(if (index < it.size - 1) " " else "")
//                                    }
//                                }.toString()
//                            } ?: EMPTY_ERROR
//                } ?: EMPTY_ERROR
                EMPTY_ERROR
            } ?: EMPTY_ERROR
}