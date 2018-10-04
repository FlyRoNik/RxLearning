package com.rxlearning.ui.screen.info

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import com.rxlearning.R
import com.rxlearning.extensions.fromHtml
import com.rxlearning.readRawResource
import com.rxlearning.ui.base.BaseLifecycleFragment
import com.rxlearning.ui.base.FragmentArgumentDelegate
import kotlinx.android.synthetic.main.fragment_info.*

class InfoFragment : BaseLifecycleFragment<InfoViewModel>() {
    override val viewModelClass = InfoViewModel::class.java
    override val layoutId = R.layout.fragment_info

    companion object {
        fun newInstance(type: TypeInfo) = InfoFragment().apply { this.type = type }
    }

    override fun getScreenTitle() = when (type) {
        TypeInfo.TERMS_OF_USE -> R.string.title_terms_of_use
        TypeInfo.PRIVACY_POLICY -> R.string.title_privacy_policy
    }

    override fun hasToolbar() = true
    override fun getToolbarId() = R.id.toolbar

    override fun observeLiveData() {
        //Do nothing
    }

    private var type by FragmentArgumentDelegate<TypeInfo>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(tvInfo) {
            text = readRawResource(
                    when (type) {
                        TypeInfo.TERMS_OF_USE -> R.raw.terms_of_use
                        TypeInfo.PRIVACY_POLICY -> R.raw.privacy_policy
                    }
            ).fromHtml()
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

}