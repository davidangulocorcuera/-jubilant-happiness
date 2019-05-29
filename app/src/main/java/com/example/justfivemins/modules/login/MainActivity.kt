package com.example.justfivemins.modules.login


import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.findNavController
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseActivity


class MainActivity: BaseActivity() {



    override fun onCreateViewId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        this.findNavController(R.id.navHostActivity)

    }

    override fun onResume() {
        super.onResume()
        showProgress(false,false)
    }

}
