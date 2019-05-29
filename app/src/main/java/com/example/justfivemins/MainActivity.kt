package com.example.justfivemins


import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.justfivemins.modules.base.BaseActivity
import kotlinx.android.synthetic.main.main_activity.*
import org.jetbrains.anko.contentView


class MainActivity(
) : BaseActivity() {



    override fun onCreateViewId(): Int {
        return R.layout.main_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        val navController =   this.findNavController(R.id.navHostMainActivity)

    }

    override fun onResume() {
        super.onResume()
        showProgress(false,false)
    }

}
