package com.rxlearning.ui.screen.main.navigation.home

import com.rxlearning.R
import com.rxlearning.ui.base.BaseLifecycleFragment

class HomeFragment : BaseLifecycleFragment<HomeViewModel>() {
    override val viewModelClass = HomeViewModel::class.java
    override val layoutId = R.layout.fragment_home

    override fun getScreenTitle() = NO_TITLE
    override fun hasToolbar() = false
    override fun getToolbarId() = NO_TOOLBAR

    override fun observeLiveData() {
        //do nothing
    }

}