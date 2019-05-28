package com.example.justfivemins.api.firebase

import android.graphics.Bitmap
import android.util.Log
import com.example.justfivemins.api.filesManager.FilesEventsListeners
import com.example.justfivemins.api.filesManager.FilesManager
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream



class FirebaseFilesManager( private val uploadImageListener: FilesEventsListeners.UploadProfileImageListener? = null): FilesManager {

    private val storageRef = FirebaseStorage.getInstance().reference

    override fun uploadProfileImage(img: Bitmap, idImg: String) {

        val profileImagesRef = storageRef.child("usersImages/profilePictures/$idImg.jpg")
        val baos = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = profileImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            uploadImageListener?.isImageUploaded(true)
            Log.v("taag", it.message)
        }.addOnSuccessListener {
            profileImagesRef.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                uploadImageListener?.isUrlSaved(true,url)
            }


        }
    }



}