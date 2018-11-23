package com.rxlearning.network.bean

import com.rxlearning.models.user.RoleType

data class UserBean(val id: String? = "",
                    val firstName: String? = "",
                    val lastName: String? = "",
                    val email: String? = "",
                    val phone: String? = "",
                    val role: RoleType? = RoleType.STUDENT,
                    val avatar: String? = "")