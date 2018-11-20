package com.rxlearning.network.bean

data class GroupBean(val id: String? = "",
                     val name: String? = "",
                     val ownerId: String? = "",
                     val students: List<String>? = listOf(),
                     val subjects: List<String>? = listOf(),
                     val infoStudents: List<Map<String, String>>? = listOf(),
                     val infoSubjects: List<Map<String, String>>? = listOf())