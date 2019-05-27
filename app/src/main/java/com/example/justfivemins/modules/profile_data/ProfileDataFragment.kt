package com.example.justfivemins.modules.profile_data


import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.Toast
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.requests.UpdateUserRequest
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.modules.base.BaseFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_profile_data.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import android.media.ExifInterface
import android.net.Uri
import android.graphics.Matrix
import android.provider.MediaStore
import android.content.Context


class ProfileDataFragment : BaseFragment(), ProfileDataPresenter.View, ApiEventsListeners.UpdateUserListener {

    private val presenter: ProfileDataPresenter by lazy { ProfileDataPresenter(this) }
    private val PICK_IMAGE_REQUEST = 1


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

    fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity?.startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


    override fun onDestroy() {
        enableDrawerMenu(true)
        showToolbar()
        super.onDestroy()
    }


    private fun setTextViews() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                val imgUri: Uri = data.data!!
                ivProfileImage.setImageBitmap(processImage(imgUri))

                return
            }

        }
        //super.onActivityResult(requestCode, resultCode, data);

    }


    fun compressAsJpeg(img: Bitmap): Bitmap {
        val original: Bitmap = img
        val out: ByteArrayOutputStream = ByteArrayOutputStream()
        original.compress(Bitmap.CompressFormat.JPEG, 100, out)
        return BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))

    }

    private fun processImage(uri: Uri): Bitmap{
        val imgBitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)

        val bytes = ByteArrayOutputStream()
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        // path is null
        val path = MediaStore.Images.Media.insertImage(activity?.applicationContext?.contentResolver, imgBitmap, "img_added", null)
        val exif = ExifInterface(path)

        val rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val rotationInDegrees = exifToDegrees(rotation)

        val matrix = Matrix()
        if (rotation != 0)matrix.preRotate(rotationInDegrees.toFloat())
//      return   compressAsJpeg( Bitmap.createBitmap(imgBitmap,
//            0, 0, imgBitmap.width, imgBitmap.height, matrix, true))
        return compressAsJpeg(imgBitmap)
    }



    private fun exifToDegrees(exifOrientation: Int): Int {

        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
    }


}