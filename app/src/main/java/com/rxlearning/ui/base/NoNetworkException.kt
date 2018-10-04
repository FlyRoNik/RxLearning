package com.rxlearning.ui.base

import com.rxlearning.R
import com.rxlearning.RxLearningApp


class NoNetworkException : Exception() {

    companion object {
        private val ERROR_MESSAGE = RxLearningApp.instance.getString(R.string.no_internet_connection)
    }

    override val message = ERROR_MESSAGE
}
