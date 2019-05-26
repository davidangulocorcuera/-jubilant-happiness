package com.example.justfivemins.utils

import android.content.Context
import android.view.View
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File


object ImageCompressor {
    fun compressImage(view: View, image: File, context: Context){
        var imageCompressed: File
        Compressor(context)
            .compressToFileAsFlowable(image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<File> { file ->
                imageCompressed = file
            }, Consumer<Throwable> { throwable ->
                throwable.printStackTrace()
                // Failed
            })
    }
}

