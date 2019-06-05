package com.example.justfivemins.api.filesManager

/**
 * Implement this interface for listen file server responses
 * */
interface FilesEventsListeners {
    interface UploadProfileImageListener {
        fun isImageUploaded(success: Boolean)
        fun isUrlSaved(success: Boolean, url: String)

    }
}