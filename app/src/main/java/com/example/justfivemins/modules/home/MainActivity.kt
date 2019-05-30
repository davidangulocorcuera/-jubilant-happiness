package com.example.justfivemins.modules.home


import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseActivity


class MainActivity : BaseActivity() {


    override fun onCreateViewId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

    }

    override fun onResume() {
        super.onResume()
        showProgress(false, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        showProgress(show = true, hasShade = true)
        val sharedViewModel = ViewModelProviders.of(this).get(MainViewModel()::class.java)
        sharedViewModel.picture.postValue(data?.data)
    }



}
