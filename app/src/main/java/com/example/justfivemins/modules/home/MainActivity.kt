package com.example.justfivemins.modules.home


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseActivity
import android.provider.MediaStore
import com.example.justfivemins.api.filesManager.FilesEventsListeners
import com.example.justfivemins.model.CurrentUser
import java.io.File
import java.io.IOException
import java.util.*
import android.widget.Toast
import android.util.DisplayMetrics




class MainActivity : BaseActivity(),  FilesEventsListeners.UploadProfileImageListener  {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel()::class.java)
    }
    lateinit var myLocale: Locale
    var currentLanguage = "es"
    lateinit var currentLang: String

    override fun onCreateViewId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
       // currentLanguage = intent.getStringExtra(currentLang)
        setLocale("es")

    }

    override fun onResume() {
        super.onResume()
        showProgress(false, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        showProgress(show = true, hasShade = true)
        data?.data?.let {
            mainViewModel.uploadProfileImage(adjustaProfilePicture(it),this)

        }
    }

    override fun isUrlSaved(success: Boolean, url: String) {
        if (success) {
             mainViewModel.url.postValue(url)
            CurrentUser.user?.profileImageUrl = url
        } else {

        }

    }

    fun adjustaProfilePicture(selectedPicture: Uri): Bitmap {

        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = this.contentResolver?.query(selectedPicture, filePathColumn, null, null, null)
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val picturePath = cursor?.getString(columnIndex!!)
        cursor?.close()

        var loadedBitmap = BitmapFactory.decodeFile(picturePath)

        var exif: ExifInterface? = null
        try {
            val pictureFile = File(picturePath)
            exif = ExifInterface(pictureFile.absolutePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var orientation = ExifInterface.ORIENTATION_NORMAL

        if (exif != null)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                loadedBitmap = rotateBitmap(loadedBitmap, 90)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                loadedBitmap = rotateBitmap(loadedBitmap, 180)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                loadedBitmap = rotateBitmap(loadedBitmap, 270)
            }
        }
        return loadedBitmap

    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix: Matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
    override fun isImageUploaded(success: Boolean) {
        if (success) {

        } else {

        }
    }

    fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            myLocale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = myLocale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(this, MainActivity::class.java)
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(this@MainActivity, "Language already selected!", Toast.LENGTH_SHORT).show()
        }
    }


}
