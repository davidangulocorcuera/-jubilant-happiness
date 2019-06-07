package com.example.justfivemins.modules.profile_data


import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.filesManager.FilesEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.requests.UpdateUserRequest
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.home.MainViewModel
import com.example.justfivemins.utils.showError
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_profile_data.*
import org.aviran.cookiebar2.CookieBar


class ProfileDataFragment : BaseFragment(), ProfileDataPresenter.View, ApiEventsListeners.UpdateUserListener,
    FilesEventsListeners.UploadProfileImageListener {

    private val presenter: ProfileDataPresenter by lazy { ProfileDataPresenter(this) }
    private val PICK_IMAGE_REQUEST = 1
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel()::class.java)
    }
    private var profileBitmap: Bitmap? = null

    override fun onCreateViewId(): Int {
        return R.layout.fragment_profile_data
    }

    override fun onResume() {
        super.onResume()
        enableScreenOnUpdate(true)
        showProgress(false, false)

    }

    override fun viewCreated(view: View?) {
        etName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                etName.postDelayed({

                    presenter.isValidName(p0.toString())
                    presenter.onChange(retrieveRegisterData())
                }, 400)

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        setTextViews()


        btnNext.setOnClickListener {
            enableScreenOnUpdate(false)
            profileBitmap?.let {
                mainViewModel.uploadProfileImage(it, this)
                showProgress(true, true)
            }
        }
        ivProfileImage.setOnClickListener {
            showProgress(true, true)
            requestPermissions()
        }
        btnBack.setOnClickListener {
            hideKeyboard()
            Navigation.findNavController(it).popBackStack()

        }
        mainViewModel.profileImageBitmap.observe(this, Observer { bitmap ->
            bitmap?.let {
                profileBitmap = it
                Glide
                    .with(this)
                    .load(bitmap)
                    .centerCrop()
                    .into(ivProfileImage)
                showProgress(false, false)
            }
        })
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity?.startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun updateUser(data: UpdateUserRequest) {
        val firebaseApiManager: FirebaseApiManager by lazy {
            FirebaseApiManager(
                updateUserListener = this,
                activity = activity!!
            )
        }
        firebaseApiManager.updateUserData(data, CurrentUser.firebaseUser!!.uid)
    }

    private fun setTextViews() {
        etName.setText(CurrentUser.user?.name)
        etSurname.setText(CurrentUser.user?.surname)
        etUniversity.setText(CurrentUser.user?.universityName)
        etJob.setText(CurrentUser.user?.jobName)
        etDescription.setText(CurrentUser.user?.description)
        if (CurrentUser.user?.profileImageUrl!!.isNotEmpty()) {
            Glide
                .with(this)
                .load(CurrentUser.user?.profileImageUrl)
                .centerCrop()
                .into(ivProfileImage)

        } else {
            ivProfileImage.setImageResource(R.drawable.no_profile_image)
        }

    }

    private fun retrieveRegisterData(): UpdateUserRequest {
        val updateUserRequest = UpdateUserRequest()
        updateUserRequest.name = etName.text.toString()
        updateUserRequest.surname = etSurname.text.toString()
        updateUserRequest.description = etDescription.text.toString()
        updateUserRequest.university = etUniversity.text.toString()
        updateUserRequest.job = etJob.text.toString()
        updateUserRequest.profileImageUrl = CurrentUser.user?.profileImageUrl!!
        return updateUserRequest
    }


    private fun enableScreenOnUpdate(enable: Boolean) {
        tiName.isEnabled = enable
        btnNext.isEnabled = enable
        tiJob.isEnabled = enable
        tiDescription.isEnabled = enable
        tiSurname.isEnabled = enable
        tiUniversity.isEnabled = enable
        ivProfileImage.isEnabled = enable
    }


    private fun requestPermissions() {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        chooseImage()
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permenantly, navigate user to app settings
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }

    override fun showNameError(error: Boolean) {
        if (error) {
            tiName.error = "Invalid name or empty"
        }
        tiName.showError(error)
    }

    override fun enableEdit(enable: Boolean) {
        btnNext.isEnabled = enable
    }

    override fun hideInputErrors() {
        tiName.isErrorEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        CookieBar.dismiss(activity)
    }
    override fun isImageUploaded(success: Boolean) {
    }

    override fun isUserUpdated(success: Boolean) {
        showProgress(false, false)
        if (success) {
            view?.let {
                Navigation.findNavController(it).popBackStack()
            }
            enableScreenOnUpdate(true)
        } else {
            CookieBar.build(activity)
                .setCookiePosition(CookieBar.BOTTOM)
                .setBackgroundColor(R.color.green_notification)
                .setMessage(getString(R.string.error_saving_data))
                .show()
            enableScreenOnUpdate(true)
        }
    }

    override fun isUrlSaved(success: Boolean, url: String) {
        if (success) {
            CurrentUser.user?.profileImageUrl = url
            updateUser(retrieveRegisterData())
        } else {

        }

    }



}