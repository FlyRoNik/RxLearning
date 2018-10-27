package com.rxlearning.ui.screen.auth.sign_up

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.EditText
import com.rxlearning.R
import com.rxlearning.extensions.*
import com.rxlearning.models.user.SignUp
import com.rxlearning.ui.base.BaseLifecycleFragment
import com.rxlearning.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.fragment_sign_up.*

class SignUpFragment : BaseLifecycleFragment<SignUpViewModel>(), View.OnClickListener {
    override val viewModelClass = SignUpViewModel::class.java
    override val layoutId = R.layout.fragment_sign_up

    override fun getScreenTitle() = NO_TITLE
    override fun hasToolbar() = false
    override fun getToolbarId() = NO_TOOLBAR

    override fun observeLiveData() {
        with(viewModel) {
            signUpLiveData.observe(this@SignUpFragment, signUpLiveDataObserver)
            setLoadingLiveData(signUpLiveData)
        }
    }

    private val signUpLiveDataObserver = Observer<Pair<String, SignUp>> {
        safeLet(it, signUpCallback) { pair, callback -> callback.showConfirmScreen(pair.first, pair.second) }
    }
    private var signUpCallback: SignUpCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        signUpCallback = bindInterfaceOrThrow<SignUpCallback>(parentFragment, context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textInfo = getString(R.string.i_accept_the_terms_of_use)
        val termsOfUse = getString(R.string.terms_of_use)
        with(tvTextInfo) {
            text = SpannableStringBuilder(textInfo).apply {
                setClickableSpan(context, termsOfUse, textInfo, R.color.text_darkCerulean, R.font.roboto_regular, R.color.bg_white, true) {
                    signUpCallback?.showTermsOfUseScreen()
                }
            }
            movementMethod = LinkMovementMethod.getInstance()
        }
        etPhone.addTextWatcher(object : SimpleTextWatcher({ text ->
            etPhone.takeIf { text.isOneSign() && text != getString(R.string.phone_plus) }?.run {
                setText(getString(R.string.phone_plus).plus(text))
                setSelectionEnd()
            }
            etPhone.takeIf { text.isOneSign() && text == getString(R.string.phone_plus) }?.apply {
                clear()
                setSelectionEnd()
            }
        }) {})
        with(etPhone) {
            addTextWatcher(PhoneNumberFormattingTextWatcher())
            setOnFocusChangeListener { v, hasFocus ->
                with(v as EditText) {
                    if (hasFocus && text.isEmpty()) {
                        setText(getString(R.string.gb_phone_code))
                        setSelection(length())
                    }
                }
            }
        }
        setClickListeners(tvSignUp)
    }

    override fun onDetach() {
        signUpCallback = null
        super.onDetach()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvSignUp -> viewModel.signUp(cbTermsOfUse.isChecked, etFirstName.getStringText(),
                    etLastName.getStringText(), etEmail.getStringText(), etPhone.getStringText())
        }
    }
}