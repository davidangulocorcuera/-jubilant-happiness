package com.example.justfivemins.api

import android.graphics.Bitmap
import android.net.Uri

interface FilesManager {
  fun uploadProfileImage(img: Bitmap, idImg: String)
}