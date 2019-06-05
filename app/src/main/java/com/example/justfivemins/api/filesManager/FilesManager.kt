package com.example.justfivemins.api.filesManager

import android.graphics.Bitmap
import android.net.Uri

/**
 * Implement this interface if you gonna change the files storage server
 * */
interface FilesManager {
  fun uploadProfileImage(img: Bitmap, userPath: String , imageName: String)
}