package com.example.justfivemins.modules.profile_data



import android.Manifest
import android.view.View
import android.widget.Toast
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.requests.UpdateUserRequest
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.modules.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile_data.*
import android.graphics.Bitmap
import android.net.Uri
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import android.content.Intent
import android.app.Activity.RESULT_OK
import android.util.Log
import java.io.IOException


class ProfileDataFragment : BaseFragment(), ProfileDataPresenter.View, ApiEventsListeners.UpdateUserListener{

    private val presenter: ProfileDataPresenter by lazy { ProfileDataPresenter(this) }
    private val PICK_IMAGE_REQUEST = 1
    private val STORAGE_PERMISSION_CODE = 123
    private val bitmap: Bitmap? = null
    private val filePath: Uri? = null


    override fun onCreateViewId(): Int {
        return R.layout.fragment_profile_data
    }

    override fun viewCreated(view: View?) {
        setTextViews()
        enableDrawerMenu(false)
        btnNext.setOnClickListener {
            updateUser(retrieveRegisterData())
        }
        ivProfileImage.setOnClickListener {
            requestPermissions()
        }
        hideToolbar()
        btnBack.setOnClickListener {
            navigator.addBackStack(false).navigateToHome()
        }

    }

    //method to show file chooser
    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }


    override fun onDestroy() {
        enableDrawerMenu(true)
        showToolbar()
        super.onDestroy()
    }


    private fun setTextViews(){
        etName.setText(CurrentUser.user?.name)
        etSurname.setText(CurrentUser.user?.surname)
        etUniversity.setText(CurrentUser.user?.universityName)
        etJob.setText(CurrentUser.user?.jobName)
        etDescription.setText(CurrentUser.user?.description)
    }

    private fun retrieveRegisterData(): UpdateUserRequest {
        val updateUserRequest = UpdateUserRequest()
        updateUserRequest.name = etName.text.toString()
        updateUserRequest.surname = etSurname.text.toString()
        updateUserRequest.description = etDescription.text.toString()
        updateUserRequest.university = etUniversity.text.toString()
        updateUserRequest.job = etJob.text.toString()
        return updateUserRequest
    }



    private fun enableScreenOnUpdate(enable: Boolean) {
        tiName.isEnabled = enable
        btnNext.isEnabled = enable
        tiJob.isEnabled = enable
        tiDescription.isEnabled = enable
        tiSurname.isEnabled = enable
        tiUniversity.isEnabled = enable
    }
    override fun isUserUpdated(success: Boolean) {
        showProgress(show = false, hasShade = false)
        enableScreenOnUpdate(true)
        if (success) {
             navigator.addBackStack(false).finishCurrent(true).navigateToHome()
        } else {
            Toast.makeText(
                activity?.applicationContext, "Register failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUser(data: UpdateUserRequest) {
        enableScreenOnUpdate(false)
        showProgress(show = true, hasShade = true)
        val firebaseApiManager: FirebaseApiManager by lazy {
            FirebaseApiManager(
                updateUserListener = this,
                activity = activity!!
            )
        }
        return firebaseApiManager.updateUserData(data, CurrentUser.firebaseUser!!.uid)
    }
    private fun requestPermissions() {
        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        showFileChooser()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.v("taag", "He llegado al fragment")

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                return
            }
            try {

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        //super.onActivityResult(requestCode, resultCode, data); comment this unless you

    }


}