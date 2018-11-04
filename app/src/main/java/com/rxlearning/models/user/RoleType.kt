package com.rxlearning.models.user

enum class RoleType(private val value: Int) {
    STUDENT(0),
    LECTURER(1);

    operator fun invoke() = value

    companion object {
        private val map = values().associateBy(RoleType::value)
        fun fromValue(value: Int?) = map[value] ?: STUDENT
    }
}