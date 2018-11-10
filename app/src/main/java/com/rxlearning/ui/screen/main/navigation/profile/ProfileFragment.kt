package com.rxlearning.ui.screen.main.navigation.profile

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.method.KeyListener
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import com.cleveroad.bootstrap.kotlin_core.utils.FileUtils
import com.cleveroad.bootstrap.kotlin_core.utils.ImageUtils
import com.cleveroad.bootstrap.kotlin_core.utils.withNotNull
import com.cleveroad.bootstrap.kotlin_permissionrequest.PermissionRequest
import com.cleveroad.bootstrap.kotlin_permissionrequest.PermissionResult
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.rxlearning.MAX_SIZE_IMAGE
import com.rxlearning.R
import com.rxlearning.RxLearningApp
import com.rxlearning.extensions.*
import com.rxlearning.models.user.User
import com.rxlearning.ui.base.BaseLifecycleFragment
import com.rxlearning.ui.base.RequestCodes
import com.rxlearning.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.util.*


class ProfileFragment : BaseLifecycleFragment<ProfileViewModel>(), View.OnClickListener {
    override val viewModelClass = ProfileViewModel::class.java
    override val layoutId = R.layout.fragment_profile

    override fun getScreenTitle() = NO_TITLE
    override fun hasToolbar() = true
    override fun getToolbarId() = R.id.toolbar
    override fun needKeyboardListener() = true
    override fun needToShowBackNav() = false

    override fun observeLiveData() {
        with(viewModel) {
            loadUserLiveData.observe(this@ProfileFragment, loadUserLiveDataObserver)
            editUserLiveData.observe(this@ProfileFragment, editUserLiveDataObserver)
            editAvatarLiveData.observe(this@ProfileFragment, editAvatarLiveDataObserver)
            logoutLiveData.observe(this@ProfileFragment, logoutLiveDataObserver)
            setLoadingLiveData(loadUserLiveData, editUserLiveData, editAvatarLiveData, logoutLiveData)
        }
    }

    override fun onKeyboardSwitch(isShow: Boolean) {
        if (userVisibleHint) {
            if (isShow) {
                profileCallback?.hideNavigationBar()
                nsScrollContainer.layoutParams = (nsScrollContainer.layoutParams as LinearLayout.LayoutParams)
                        .apply { bottomMargin = 0 }
                cvButtonContainer.hide(true)
                tvSave.setText(R.string.menu_item_save)
                editAction.removeLongClickListener(etFirstName, etLastName, etEmail, etPhone)
            } else {
                profileCallback?.showNavigationBar()
                nsScrollContainer.layoutParams = (nsScrollContainer.layoutParams as LinearLayout.LayoutParams)
                        .apply { bottomMargin = resources.getDimensionPixelSize(R.dimen.navigation_bar_height) }
                cvButtonContainer.show()
                tvSave.setText(R.string.menu_item_edit)
                removeKeyListener(etFirstName, etLastName, etEmail, etPhone)
                editAction.setLongClickListener(etFirstName, etLastName, etEmail, etPhone)
                viewModel.loadUser()
            }
        }
    }

    private val permissionRequest: PermissionRequest = PermissionRequest()
    private val phoneNumberFormattingTextWatcher = PhoneNumberFormattingTextWatcher()
    private val phoneNumberUtil = PhoneNumberUtil.getInstance()
    private val editUserLiveDataObserver = Observer<User> { updateProfile(it) }
    private val loadUserLiveDataObserver = Observer<User> { updateProfile(it) }
    private val editAvatarLiveDataObserver = Observer<String> { setAvatar(it) }
    private val logoutLiveDataObserver = Observer<Unit> { RxLearningApp.instance.onLogout() }
    private val editAction = View.OnLongClickListener {
        addKeyListener(etFirstName, etLastName, etEmail, etPhone)
        it.requestFocus()
        showKeyboard()
        true
    }
    private var profileCallback: ProfileCallback? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        profileCallback = bindInterfaceOrThrow<ProfileCallback>(parentFragment, context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners(tvSave, flChangePassword, flLogout, tvPickPhoto)
        etPhone.addTextChangedListener(phoneNumberFormattingTextWatcher)
        etPhone.setOnFocusChangeListener { v, hasFocus ->
            with(v as EditText) {
                if (hasFocus && text.isEmpty()) {
                    setText(getString(R.string.ua_phone_code))
                    setSelection(length())
                }
            }
        }
        etPhone.addTextWatcher(SimpleTextWatcher({ text ->
            etPhone.takeIf { text.isOneSign() && text != getString(R.string.phone_plus) }?.run {
                setText(getString(R.string.phone_plus).plus(text))
                setSelectionEnd()
            }
            etPhone.takeIf { text.isOneSign() && text == getString(R.string.phone_plus) }?.apply {
                clear()
                setSelectionEnd()
            }
        }))
        etPhone.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.editUser(etFirstName.getStringText(), etLastName.getStringText(), etEmail.getStringText(), etPhone.getStringText())
                true
            } else {
                false
            }

        }
        viewModel.loadUser()
        removeKeyListener(etFirstName, etLastName, etEmail, etPhone)
        //TODO turn on edit mode for long click
        //editAction.setLongClickListener(etFirstName, etLastName, etEmail, etPhone)
    }

    private fun updateProfile(user: User?) {
        withNotNull(user) {
            etFirstName.setText(firstName)
            etLastName.setText(lastName)
            etEmail.setText(email)
            etPhone.setText(phoneNumberUtil.format(phoneNumberUtil.parse(phone, Locale.getDefault().country),
                    PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL))
            setAvatar(avatar)
        }
    }

    override fun onDestroyView() {
        etPhone.removeTextChangedListener(phoneNumberFormattingTextWatcher)
        super.onDestroyView()
    }

    override fun onDetach() {
        profileCallback = null
        super.onDetach()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == RequestCodes.RequestCode.REQUEST_PICK_IMAGE_FROM_GALLERY()) {
            data?.data?.let { pickImageFromGalleryResult(it) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvSave -> {
                toast("Not implement")
                //TODO need implement
//                if (cvButtonContainer.visibility != View.VISIBLE) {
//                    viewModel.editUser(etFirstName.getStringText(), etLastName.getStringText(), etEmail.getStringText(), etPhone.getStringText())
//                } else {
//                    etFirstName.performLongClick()
//                }
            }
            R.id.flChangePassword -> profileCallback?.showChangePasswordScreen()
            R.id.flLogout -> RxLearningApp.instance.onLogout()
            R.id.tvPickPhoto -> {
                toast("Not implement")
                //TODO need implement
                //pickPhoto()
            }
        }
    }

    private fun pickPhoto() {
        permissionRequest.request(this, RequestCodes.RequestCode.REQUEST_WRITE_EXTERNAL_STORAGE(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                object : PermissionResult {
                    override fun onPermissionGranted() {
                        profileCallback?.onStartActivityForResult(ImageUtils.createImagePickIntentFromGallery(ctx), RequestCodes.RequestCode.REQUEST_PICK_IMAGE_FROM_GALLERY())
                    }
                })
    }

    private fun addKeyListener(vararg editTexts: EditText) =
            editTexts.forEach { it.keyListener = it.tag as KeyListener }

    private fun removeKeyListener(vararg editTexts: EditText) =
            editTexts.forEach {
                it.tag = it.keyListener
                it.keyListener = null
            }

    private fun showKeyboard() =
            (act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

    private fun pickImageFromGalleryResult(uri: Uri) = FileUtils.getSmartFilePath(ctx, uri)?.let { pickImage(it) }

    private fun pickImage(imagePath: String) {
        if (imagePath.isNotEmpty()) {
            ImageUtils.compressImageFromUri(Uri.fromFile(File(imagePath)), maxSize = MAX_SIZE_IMAGE)?.let {
                File(imagePath).takeIf { file -> ImageUtils.saveBitmap(file, ImageUtils.modifyImageToNormalOrientation(it, imagePath)) }
            }?.let { viewModel.editAvatar(it.path) }
                    ?: onError(getString(R.string.image_error))
        }
    }

    private fun setAvatar(avatar: String?) =
            avatar.takeUnless { it.isNullOrEmpty() }?.let { ivAvatar.loadImageCircleCrop(it, R.drawable.ic_userpic_blue_24dp) }
                    ?: ivAvatar.setDrawable(R.drawable.ic_userpic_blue_24dp)
}