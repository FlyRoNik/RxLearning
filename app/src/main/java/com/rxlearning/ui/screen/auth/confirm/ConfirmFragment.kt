package com.rxlearning.ui.screen.auth.confirm

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.cleveroad.bootstrap.kotlin_core.utils.misc.MiscellaneousUtils.getExtra
import com.cleveroad.bootstrap.kotlin_core.utils.misc.hideKeyboard
import com.google.firebase.auth.PhoneAuthCredential
import com.rxlearning.EMPTY_STRING_VALUE
import com.rxlearning.KEYBOARD_HIDE_DELAY_MILLISECONDS
import com.rxlearning.R
import com.rxlearning.extensions.clear
import com.rxlearning.extensions.getStringText
import com.rxlearning.extensions.safeLet
import com.rxlearning.extensions.setClickListeners
import com.rxlearning.models.user.SignUp
import com.rxlearning.ui.base.BaseLifecycleFragment
import com.rxlearning.utils.SimpleTextWatcher
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_confirm.*
import org.jetbrains.anko.support.v4.act
import java.util.concurrent.TimeUnit


class ConfirmFragment : BaseLifecycleFragment<ConfirmViewModel>(), View.OnClickListener {
    override val viewModelClass = ConfirmViewModel::class.java
    override val layoutId = R.layout.fragment_confirm

    companion object {
        private val TOKEN_EXTRA = getExtra("TOKEN_EXTRA", ConfirmFragment::class.java)
        private val SIGN_UP_USER_EXTRA = getExtra("SIGN_UP_USER_EXTRA", ConfirmFragment::class.java)
        fun newInstance(token: String, signUp: SignUp) = ConfirmFragment().apply {
            arguments = Bundle().apply {
                putString(TOKEN_EXTRA, token)
                putParcelable(SIGN_UP_USER_EXTRA, signUp)
            }
        }
    }

    override fun getScreenTitle() = R.string.confirm_screen_title
    override fun hasToolbar() = true
    override fun getToolbarId() = R.id.toolbar
    override fun hasResultFromDialog() = true

    override fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent) {
        activeDigit = true
        etSixthDigit.clearFocus()
        showKeyboard()
    }

    override fun observeLiveData() {
        with(viewModel) {
            confirmLiveData.observe(this@ConfirmFragment, confirmLiveDataObserver)
            resendLiveData.observe(this@ConfirmFragment, resendLiveDataObserver)
            setLoadingLiveData(confirmLiveData, resendLiveData)
        }
    }

    private val confirmLiveDataObserver = Observer<PhoneAuthCredential> {
        safeLet(it, confirmCallback, arguments?.getParcelable<SignUp>(SIGN_UP_USER_EXTRA)) { credential, callback, signUp ->
            callback.showPasswordScreen(credential, signUp)
            clear(etFirstDigit, etSecondDigit, etThirdDigit, etFourthDigit, etFifthDigit, etSixthDigit)
        }
    }
    private val resendLiveDataObserver = Observer<String> { arguments?.putString(TOKEN_EXTRA, it) }
    private val sendDigit: () -> Unit = {
        if (activeDigit && isNotEmpty(etFirstDigit, etSecondDigit, etThirdDigit, etFourthDigit, etFifthDigit, etSixthDigit)) {
            activeDigit = false
            hideKeyboard()
            arguments?.getString(TOKEN_EXTRA)?.let { token ->
                viewModel.confirm(token,
                        "${etFirstDigit.getStringText()}${etSecondDigit.getStringText()}" +
                                "${etThirdDigit.getStringText()}${etFourthDigit.getStringText()}" +
                                "${etFifthDigit.getStringText()}${etSixthDigit.getStringText()}")
            }
        }
    }
    private var confirmCallback: ConfirmCallback? = null
    private var activeDigit = true

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        confirmCallback = bindInterfaceOrThrow<ConfirmCallback>(parentFragment, context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyClearWhenFocus(etFirstDigit, etSecondDigit, etThirdDigit, etFourthDigit, etFifthDigit, etSixthDigit)
        etFirstDigit.requestFocus()
        showKeyboard()

        etFirstDigit.addTextWatcher(object : SimpleTextWatcher({ if (!it.isEmpty()) etSecondDigit.requestFocus() }) {})
        etSecondDigit.addTextWatcher(object : SimpleTextWatcher({ if (!it.isEmpty()) etThirdDigit.requestFocus() }) {})
        etThirdDigit.addTextWatcher(object : SimpleTextWatcher({ if (!it.isEmpty()) etFourthDigit.requestFocus() }) {})
        etFourthDigit.addTextWatcher(object : SimpleTextWatcher({ if (!it.isEmpty()) etFifthDigit.requestFocus() }) {})
        etFifthDigit.addTextWatcher(object : SimpleTextWatcher({ if (!it.isEmpty()) etSixthDigit.requestFocus() }) {})
        etSixthDigit.addTextWatcher(object : SimpleTextWatcher({ if (!it.isEmpty()) sendDigit() }) {})

        etFirstDigit.setOnKeyListener { _, keyCode, event -> false.apply { isSetDigit(event, keyCode) } }
        etSecondDigit.setOnKeyListener { _, keyCode, event -> false.apply { isSetDigit(event, keyCode) { etFirstDigit.requestFocus() } } }
        etThirdDigit.setOnKeyListener { _, keyCode, event -> false.apply { isSetDigit(event, keyCode) { etSecondDigit.requestFocus() } } }
        etFourthDigit.setOnKeyListener { _, keyCode, event -> false.apply { isSetDigit(event, keyCode) { etThirdDigit.requestFocus() } } }
        etFifthDigit.setOnKeyListener { _, keyCode, event -> false.apply { isSetDigit(event, keyCode) { etFourthDigit.requestFocus() } } }
        etSixthDigit.setOnKeyListener { _, keyCode, event -> false.apply { isSetDigit(event, keyCode) { etFifthDigit.requestFocus() } } }
        setClickListeners(tvResend)
    }

    override fun onStop() {
        super.onStop()
        activeDigit = true
    }

    override fun onDetach() {
        confirmCallback = null
        super.onDetach()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvResend -> arguments?.getParcelable<SignUp>(SIGN_UP_USER_EXTRA)?.let { viewModel.resend(it) }
        }
    }

    private fun showKeyboard() =
            (act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

    private fun applyClearWhenFocus(vararg editTexts: EditText) {
        editTexts.forEach { it.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) (v as EditText).setText(EMPTY_STRING_VALUE) } }
    }

    private fun clear(vararg editTexts: EditText) {
        editTexts.forEach { it.clear() }
    }

    private fun isSetDigit(event: KeyEvent, keyCode: Int, actionRemove: () -> Unit = {}) {
        if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DEL) actionRemove()
    }

    private fun isNotEmpty(vararg editTexts: EditText): Boolean {
        editTexts.forEach { if (it.getStringText().isEmpty()) return false }
        return true
    }

    override fun handleNavigation() {
        hideKeyboard()
        Flowable.timer(KEYBOARD_HIDE_DELAY_MILLISECONDS, TimeUnit.MILLISECONDS, Schedulers.io()).subscribe({ backPressed() }, { onError(it) })
    }
}