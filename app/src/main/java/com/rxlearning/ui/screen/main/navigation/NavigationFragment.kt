package com.rxlearning.ui.screen.main.navigation

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.rxlearning.R
import com.rxlearning.extensions.bindInterface
import com.rxlearning.extensions.getContextCompatColor
import com.rxlearning.ui.base.BaseFragmentStatePagerAdapter
import com.rxlearning.ui.base.BaseFragmentStatePagerAdapter.FragmentInfoContainer
import com.rxlearning.ui.base.BaseLifecycleFragment
import com.rxlearning.ui.screen.main.navigation.home.HomeFragment
import com.rxlearning.ui.screen.main.navigation.profile.ProfileCallback
import com.rxlearning.ui.screen.main.navigation.profile.ProfileFragment
import com.rxlearning.ui.screen.main.navigation.settings.SettingsCallback
import com.rxlearning.ui.screen.main.navigation.settings.SettingsFragment
import kotlinx.android.synthetic.main.fragment_navigation.*
import org.jetbrains.anko.support.v4.ctx

class NavigationFragment : BaseLifecycleFragment<NavigationViewModel>(), ProfileCallback,
        SettingsCallback {
    override val viewModelClass = NavigationViewModel::class.java
    override val layoutId = R.layout.fragment_navigation

    companion object {
        private const val PAGE_LIMIT = 5
        private const val PAGE_WITH_FAB = 1
        fun newInstance() = NavigationFragment().apply {
            arguments = Bundle()
        }
    }

    override fun getScreenTitle() = NO_TITLE
    override fun hasToolbar() = false
    override fun getToolbarId() = NO_TOOLBAR

    override fun observeLiveData() {
        //Do nothing
    }

    private var navigationCallback: NavigationCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        navigationCallback = bindInterfaceOrThrow<NavigationCallback>(parentFragment, context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(vpPagerContainer) {
            offscreenPageLimit = PAGE_LIMIT
            adapter = BaseFragmentStatePagerAdapter(ctx, childFragmentManager, getPagesList())
            setPagingEnabled(false)
        }
        with(bnPagerController) {
            addItems(listOf(
                    AHBottomNavigationItem(getString(R.string.item_home), R.drawable.ic_home_white_active_24dp),
                    AHBottomNavigationItem(getString(R.string.item_profile), R.drawable.ic_profile_white_active_24dp),
                    AHBottomNavigationItem(getString(R.string.item_settings), R.drawable.ic_settings_white_active_24dp)
            ))
            manageFloatingActionButtonBehavior(fabLocationButton)
            fabLocationButton.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
            setOnTabSelectedListener { position, wasSelected ->
                if (!wasSelected) vpPagerContainer.setCurrentItem(position, false)
                return@setOnTabSelectedListener true
            }
            defaultBackgroundColor = ctx.getContextCompatColor(R.color.bg_darkCerulean)
            setTitleTypeface(ResourcesCompat.getFont(ctx, R.font.roboto_regular))
            ctx.resources.getDimension(R.dimen.xxxx_small_text).also { setTitleTextSize(it, it) }
            isBehaviorTranslationEnabled = true
        }

        //TODO callback for fab button
//        fabLocationButton.setOnClickListener {
//            bindInterface<BaseFragmentStatePagerAdapter, NavigationButtonCallback>(vpPagerContainer.adapter, { getFragment(PAGE_WITH_FAB) }) { onClick() }
//        }
    }

    override fun onDetach() {
        navigationCallback = null
        super.onDetach()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        bindInterface<BaseFragmentStatePagerAdapter>(vpPagerContainer.adapter) {
            getFragment(vpPagerContainer.currentItem).onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onStartActivityForResult(intent: Intent, requestCode: Int) {
        navigationCallback?.onStartActivityForResult(intent, requestCode)
    }

    override fun showChangePasswordScreen() {
        navigationCallback?.showChangePasswordScreen()
    }

    override fun showPrivacyPolicyScreen() {
        navigationCallback?.showPrivacyPolicyScreen()
    }

    override fun showTermsOfUseScreen() {
        navigationCallback?.showTermsOfUseScreen()
    }

    override fun showFeedbackScreen() {
        navigationCallback?.showFeedbackScreen()
    }

    override fun hideNavigationBar() = with(bnPagerController) {
        isBehaviorTranslationEnabled = false
        hideBottomNavigation(false)
    }

    override fun showNavigationBar() = with(bnPagerController) {
        isBehaviorTranslationEnabled = true
        restoreBottomNavigation(false)
    }

    private fun getPagesList() = mutableListOf<FragmentInfoContainer>().apply {
        add(FragmentInfoContainer(HomeFragment::class.java))
        add(FragmentInfoContainer(ProfileFragment::class.java))
        add(FragmentInfoContainer(SettingsFragment::class.java))
    }

}