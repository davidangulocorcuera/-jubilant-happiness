package com.example.justfivemins.modules.gallery


import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseFragment


class GalleryFragment : BaseFragment() {
    override fun onCreateViewId(): Int {
        return R.layout.fragment_gallery
    }

    override fun viewCreated(view: View?) {
        setToolbarTitle(getString(R.string.my_photos).toUpperCase())

    }




}
