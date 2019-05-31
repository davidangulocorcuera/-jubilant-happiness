package com.example.justfivemins.api.filesManager

import android.graphics.Bitmap
import android.net.Uri

interface FilesManager {
  fun uploadProfileImage(img: Bitmap, userPath: String , imageName: String)
}