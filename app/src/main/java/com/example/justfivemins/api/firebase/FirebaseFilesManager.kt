package com.example.justfivemins.api.firebase

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.justfivemins.api.filesManager.FilesEventsListeners
import com.example.justfivemins.api.filesManager.FilesManager
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class FirebaseFilesManager( private val uploadImageListener: FilesEventsListeners.UploadProfileImageListener? = null, var activity: Activity? = null): FilesManager {

    private val storageRef = FirebaseStorage.getInstance().reference

    override fun uploadProfileImage(img: Bitmap, userPath: String , imageName: String) {

        val profileImagesRef = storageRef.child("usersImages/$userPath/$imageName.jpg")
        val baos = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.WEBP, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = profileImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            uploadImageListener?.isImageUploaded(true)
        }.addOnSuccessListener {
            profileImagesRef.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                uploadImageListener?.isUrlSaved(true,url)
            }


        }
    }





}