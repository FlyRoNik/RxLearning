package com.rxlearning.utils

import android.content.Context
import com.cleveroad.bootstrap.kotlin_validators.*
import com.rxlearning.R
import com.rxlearning.extensions.getInteger
import java.util.regex.Pattern

private val PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!*^?+-_@#\$%&]+\$")
private val NAME_PATTERN = Pattern.compile("^[A-Za-zÀ-ɏ'-]+\$")

fun emailValidator(context: Context) = with(context) {
    EmailValidator
            .builder(this)
            .emptyError(getString(R.string.email_is_empty))
            .invalidError(getString(R.string.email_is_not_valid_format))
            .build()
}

fun passwordValidator(context: Context) = with(context) {
    val minPasswordLength = getInteger(R.integer.min_password_length)
    val maxPasswordLength = getInteger(R.integer.max_password_length)
    PasswordValidator
            .builder(this)
            .additionalRegex(PASSWORD_PATTERN)
            .passwordMinLength(minPasswordLength)
            .passwordMaxLength(maxPasswordLength)
            .emptyErrorMessage(getString(R.string.password_is_empty))
            .maxLengthErrorMessage(getString(R.string.password_max_length_error, maxPasswordLength))
            .minLengthErrorMessage(getString(R.string.password_min_length_error, minPasswordLength))
            .invalidErrorMessage(getString(R.string.password_is_invalid))
            .build()
}

fun nameValidator(context: Context) = with(context) {
    val maxNameLength = getInteger(R.integer.max_name_length)
    NameValidator
            .builder(this)
            .additionalRegex(NAME_PATTERN)
            .nameMinLength(getInteger(R.integer.min_name_length))
            .nameMaxLength(maxNameLength)
            .emptyErrorMessage(getString(R.string.name_is_empty))
            .maxLengthErrorMessage(getString(R.string.name_max_length_error, maxNameLength))
            .minLengthErrorMessage(getString(R.string.name_is_empty))
            .invalidErrorMessage(getString(R.string.name_is_invalid))
            .build()
}

fun phoneValidator(context: Context) = with(context) {
    PhoneValidatorImpl
            .builder(this)
            .emptyErrorMessage(getString(R.string.phone_is_empty))
            .invalidErrorMessage(getString(R.string.phone_is_invalid))
            .build()
}

fun matchPasswordValidator(context: Context) = with(context) {
    MatchPasswordValidator
            .builder(this)
            .additionalRegex(PASSWORD_PATTERN)
            .minLength(getInteger(R.integer.min_password_length))
            .maxLength(getInteger(R.integer.max_password_length))
            .passwordEmptyErrorMessage(R.string.password_is_empty)
            .passwordInvalidErrorMessage(R.string.password_is_invalid)
            .confirmPasswordEmptyErrorMessage(R.string.confirm_password_is_empty)
            .confirmPasswordInvalidErrorMessage(R.string.confirm_password_is_invalid)
            .notMatchErrorMessage(R.string.passwords_dont_match)
            .build()
}

fun titleFeedbackValidator(context: Context) = with(context) {
    val maxFeedbackTitleLength = getInteger(R.integer.max_feedback_title_length)
    NameValidator
            .builder(this)
            .nameMaxLength(maxFeedbackTitleLength)
            .emptyErrorMessage(getString(R.string.feedback_theme_is_empty))
            .maxLengthErrorMessage(getString(R.string.feedback_theme_length_error, maxFeedbackTitleLength))
            .build()
}

fun messageFeedbackValidator(context: Context) = with(context) {
    val maxFeedbackMessageLength = getInteger(R.integer.max_feedback_message_length)
    NameValidator
            .builder(this)
            .nameMaxLength(maxFeedbackMessageLength)
            .emptyErrorMessage(getString(R.string.feedback_message_is_empty))
            .maxLengthErrorMessage(getString(R.string.feedback_message_length_error, maxFeedbackMessageLength))
            .build()
}