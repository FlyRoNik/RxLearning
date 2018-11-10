package com.rxlearning.ui.screen.main.navigation.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import com.rxlearning.R
import com.rxlearning.extensions.setClickListeners
import com.rxlearning.ui.base.BaseLifecycleFragment
import kotlinx.android.synthetic.main.fragment_settings.*

interface SettingsCallback {

    fun showPrivacyPolicyScreen()

    fun showTermsOfUseScreen()

    fun showFeedbackScreen()

}

class SettingsFragment : BaseLifecycleFragment<SettingsViewModel>(), View.OnClickListener {
    override val viewModelClass = SettingsViewModel::class.java
    override val layoutId = R.layout.fragment_settings

    override fun getScreenTitle() = NO_TITLE
    override fun hasToolbar() = false
    override fun getToolbarId() = NO_TOOLBAR

    override fun observeLiveData() {
        //Do nothing
    }

    private var settingsCallback: SettingsCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        settingsCallback = bindInterfaceOrThrow<SettingsCallback>(parentFragment, context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(flPrivacyPolicy, flTermsOfUse, flGiveUsFeedback)
    }

    override fun onDetach() {
        settingsCallback = null
        super.onDetach()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.flPrivacyPolicy -> settingsCallback?.showPrivacyPolicyScreen()
            R.id.flTermsOfUse -> settingsCallback?.showTermsOfUseScreen()
            R.id.flGiveUsFeedback -> settingsCallback?.showFeedbackScreen()
        }
    }

}