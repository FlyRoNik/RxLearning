package com.rxlearning.ui.screen.main.navigation.subjects

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.rxlearning.RxLearningApp
import com.rxlearning.models.subject.Subject
import com.rxlearning.network.GROUPS_KEY
import com.rxlearning.network.STUDENTS_KEY
import com.rxlearning.network.bean.GroupBean
import com.rxlearning.network.converters.GroupBeanConverterImpl
import com.rxlearning.ui.base.BaseViewModel
import io.reactivex.functions.Consumer

class SubjectsViewModel(application: Application) : BaseViewModel(application) {
    private val groupBeanConverter = GroupBeanConverterImpl()

    val loadSubjectsLiveData = MutableLiveData<List<Subject>>()
    val loadSubjectDetailLiveData = MutableLiveData<Subject>()
    val refreshLiveData = MutableLiveData<Boolean>()

    private val refreshErrorConsumer = Consumer<Throwable> {
        onErrorConsumer.accept(it)
        refreshLiveData.value = false
    }

    fun loadSubjects() {
        refreshLiveData.value = true
        FirebaseFirestore.getInstance()
                .collection(GROUPS_KEY)
                .whereArrayContains(STUDENTS_KEY, RxLearningApp.instance.getCurrentUser()?.uid
                        ?: "")
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val subjects: List<Subject> = it.result?.toObjects(GroupBean::class.java)
                                ?.let { group -> groupBeanConverter.convertListInToOut(group) }
                                ?.mapNotNull { group -> group.subjects }
                                ?.flatten() ?: listOf()
                        loadSubjectsLiveData.postValue(subjects)
                        refreshLiveData.value = false
                    } else {
                        refreshErrorConsumer.accept(it.exception)
                    }
                }
    }

    fun loadSubjectDetail(subject: Subject) {
        //TODO need implement
    }

}