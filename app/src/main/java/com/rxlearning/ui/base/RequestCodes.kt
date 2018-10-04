package com.rxlearning.ui.base

class RequestCodes {
    companion object {
        private var currentRequestCode = 0
    }

    enum class RequestCode {
        REQUEST_DIALOG_MESSAGE,
        REQUEST_DIALOG_RESET_PASSWORD,
        REQUEST_WRITE_EXTERNAL_STORAGE,
        REQUEST_PICK_IMAGE_FROM_GALLERY,
        REQUEST_DIALOG_CHANGE_PASSWORD,
        GEOLOCATION_REQUEST_CODE,
        REQUEST_DIALOG_FEEDBACK,
        REQUEST_DIALOG_EDIT_CARD,
        REQUEST_DIALOG_NUMBER_PICKER,
        REQUEST_DIALOG_PAY_TICKET;

        private val value = ++currentRequestCode

        operator fun invoke() = value
    }
}