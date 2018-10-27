package com.rxlearning.ui.screen.auth.sign_in

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.view.View
import com.rxlearning.R
import com.rxlearning.extensions.getStringText
import com.rxlearning.extensions.setClickListeners
import com.rxlearning.extensions.switchPassword
import com.rxlearning.ui.base.BaseLifecycleFragment
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : BaseLifecycleFragment<SignInViewModel>(), View.OnClickListener {
    override val viewModelClass = SignInViewModel::class.java
    override val layoutId = R.layout.fragment_sign_in

    override fun getScreenTitle() = NO_TITLE
    override fun hasToolbar() = false
    override fun getToolbarId() = NO_TOOLBAR

    override fun observeLiveData() {
        with(viewModel) {
            signInLiveData.observe(this@SignInFragment, signInLiveDataObserver)
            setLoadingLiveData(signInLiveData)
        }
    }

    private val signInLiveDataObserver = Observer<Unit> { signInCallback?.showMainScreen() }

    private var signInCallback: SignInCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        signInCallback = bindInterfaceOrThrow<SignInCallback>(parentFragment, context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(ivTriggerPasswordVisible, tvSignIn, tvForgotPassword)
    }

    override fun onDetach() {
        signInCallback = null
        super.onDetach()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivTriggerPasswordVisible -> etPassword.switchPassword { ivTriggerPasswordVisible.switchEye(it) }
            R.id.tvSignIn -> viewModel.signIn(etEmail.getStringText(), etPassword.getStringText())
            R.id.tvForgotPassword -> signInCallback?.showForgotPassword()
        }
    }
}