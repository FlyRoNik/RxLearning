package com.rxlearning.ui.screen.main.navigation.subjects

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.cleveroad.bootstrap.kotlin_core.utils.misc.isConnected
import com.rxlearning.R
import com.rxlearning.extensions.hide
import com.rxlearning.extensions.restfulClick
import com.rxlearning.extensions.show
import com.rxlearning.models.subject.Subject
import com.rxlearning.ui.base.BaseLifecycleFragment
import com.rxlearning.ui.base.NoNetworkException
import kotlinx.android.synthetic.main.fragment_subjects.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast

interface AddDSubjectButtonCallback {
    fun onClick()
}

class SubjectsFragment : BaseLifecycleFragment<SubjectsViewModel>(), SubjectAdapterCallback,
        SwipeRefreshLayout.OnRefreshListener, AddDSubjectButtonCallback {
    override val viewModelClass = SubjectsViewModel::class.java
    override val layoutId = R.layout.fragment_subjects

    override fun getScreenTitle() = NO_TITLE
    override fun hasToolbar() = false
    override fun getToolbarId() = NO_TOOLBAR

    override fun observeLiveData() {
        with(viewModel) {
            loadSubjectsLiveData.observe(this@SubjectsFragment, loadSubjectsLiveDataObservable)
            refreshLiveData.observe(this@SubjectsFragment, refreshObserver)
            loadSubjectDetailLiveData.observe(this@SubjectsFragment, loadSubjectDetailLiveDataObservable)
            setLoadingLiveData(loadSubjectsLiveData, refreshLiveData, loadSubjectDetailLiveData)
        }
    }

    private val loadSubjectsLiveDataObservable = Observer<List<Subject>> {
        it?.let { list ->
            subjectAdapter?.dataLoad(list)
            tvNoResults.apply { list.takeIf { list.isEmpty() }?.let { show() } ?: hide() }
        }
    }
    private val loadSubjectDetailLiveDataObservable = Observer<Subject> {
        it?.let {
            //TODO need implement
            toast("Not implemented")
        }
    }
    private val refreshObserver = Observer<Boolean> { it?.let { swRefresh.isRefreshing = it } }

    private var subjectAdapter: SubjectAdapter? = null
    private var subjectCallback: SubjectsCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        subjectCallback = bindInterfaceOrThrow<SubjectsCallback>(parentFragment, context)
    }

    override fun onDetach() {
        subjectCallback = null
        super.onDetach()
    }

    override fun onClickItem(subject: Subject) {
        arguments = arguments?.restfulClick { viewModel.loadSubjectDetail(subject) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swRefresh.setOnRefreshListener(this)
        swRefresh.setColorSchemeResources(R.color.colorPrimary)
        with(rvSubjects) {
            adapter = SubjectAdapter(ctx, this@SubjectsFragment).apply { subjectAdapter = this }
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && ctx.isConnected()) {
            if (subjectAdapter?.itemCount == 0) onRefresh() else viewModel.loadSubjects()
        }
    }

    override fun onRefresh() {
        swRefresh.isRefreshing = false
        if (ctx.isConnected()) viewModel.loadSubjects() else onError(NoNetworkException())
    }

    override fun onClick() {
        //TODO need implement
        toast("Not implemented")
    }
}