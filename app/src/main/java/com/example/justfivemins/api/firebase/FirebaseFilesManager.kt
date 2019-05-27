package com.example.justfivemins.api.firebase

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.justfivemins.api.FilesManager
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import com.google.android.gms.tasks.OnSuccessListener



class FirebaseFilesManager(): FilesManager {

    private val storageRef = FirebaseStorage.getInstance().reference

    override fun uploadProfileImage(img: Bitmap, idImg: String) {

        val profileImagesRef = storageRef.child("usersImages/profilePictures/$idImg.jpg")
        val baos = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = profileImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {

            Log.v("taag", it.message)
        }.addOnSuccessListener {
            profileImagesRef.downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                Log.v("taag", url)
            }


        }
    }



}