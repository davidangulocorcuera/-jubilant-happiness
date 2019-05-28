package com.example.justfivemins.api.filesManager

interface FilesEventsListeners {
    interface UploadProfileImageListener {
        fun isImageUploaded(success: Boolean)
        fun isUrlSaved(success: Boolean, url: String)

    }
}