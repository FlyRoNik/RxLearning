package com.rxlearning.ui.screen.auth.choose

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import com.cleveroad.bootstrap.kotlin_core.utils.misc.hideKeyboard
import com.rxlearning.R
import com.rxlearning.extensions.hide
import com.rxlearning.extensions.show
import com.rxlearning.ui.base.BaseFragmentStatePagerAdapter
import com.rxlearning.ui.base.BaseLifecycleFragment
import com.rxlearning.ui.screen.auth.sign_in.SignInFragment
import com.rxlearning.ui.screen.auth.sign_up.SignUpFragment
import com.rxlearning.utils.SimplePageChangeListener
import kotlinx.android.synthetic.main.fragment_choose.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.ctx

class ChooseFragment : BaseLifecycleFragment<ChooseViewModel>() {
    override val viewModelClass = ChooseViewModel::class.java
    override val layoutId = R.layout.fragment_choose

    companion object {
        fun newInstance() = ChooseFragment().apply {
            arguments = Bundle()
        }

        private const val PAGE_LIMIT = 2
        private const val FIRST_PAGE = 0
        private const val SECOND_PAGE = 1
    }

    override fun getScreenTitle() = NO_TITLE
    override fun hasToolbar() = false
    override fun getToolbarId() = NO_TOOLBAR
    override fun needKeyboardListener() = true

    override fun observeLiveData() {
        //Do nothing
    }

    override fun onKeyboardSwitch(isShow: Boolean) {
        if (isShow) {
            ivTitle.hide(true)
            TypedValue().also {
                if (act.theme.resolveAttribute(android.R.attr.actionBarSize, it, true)) {
                    llToolbar.layoutParams = llToolbar.layoutParams.apply {
                        height = TypedValue.complexToDimensionPixelSize(it.data, resources.displayMetrics)
                    }
                }
            }
        } else {
            ivTitle.show()
            llToolbar.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    resources.getDimensionPixelSize(R.dimen.auth_toolbar_height))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPagerAdapter = BaseFragmentStatePagerAdapter(ctx, childFragmentManager, getPagesList())
        with(vpAuth) {
            offscreenPageLimit = PAGE_LIMIT
            adapter = viewPagerAdapter
            addOnPageChangeListener(object : SimplePageChangeListener() {
                override fun onPageSelected(position: Int) {
                    hideKeyboard()
                }
            })
        }
        with(tlTabAuth) {
            setupWithViewPager(vpAuth, true)
            getTabAt(FIRST_PAGE)?.text = getString(R.string.tab_log_in)
            getTabAt(SECOND_PAGE)?.text = getString(R.string.tab_register)
        }
    }

    private fun getPagesList() = mutableListOf<BaseFragmentStatePagerAdapter.FragmentInfoContainer>().apply {
        add(BaseFragmentStatePagerAdapter.FragmentInfoContainer(SignInFragment::class.java))
        add(BaseFragmentStatePagerAdapter.FragmentInfoContainer(SignUpFragment::class.java))
    }
}