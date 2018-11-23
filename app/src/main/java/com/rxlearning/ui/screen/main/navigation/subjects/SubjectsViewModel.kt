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
import durdinapps.rxfirebase2.RxFirestore
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.addTo

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
        val query = FirebaseFirestore.getInstance()
                .collection(GROUPS_KEY)
                .whereArrayContains(STUDENTS_KEY, RxLearningApp.instance.getCurrentUser()?.uid
                        ?: "")
        RxFirestore.getCollection(query, GroupBean::class.java)
                .map(groupBeanConverter::convertListInToOut)
                .subscribe({
                    loadSubjectsLiveData.value = it.mapNotNull { group -> group.subjects }.flatten()
                    refreshLiveData.value = false
                }, refreshErrorConsumer::accept)
                .addTo(compositeDisposable)

    }

    fun loadSubjectDetail(subject: Subject) {
        //TODO need implement
    }

}