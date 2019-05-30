package com.example.justfivemins.modules.downloadingDataScreen

import android.app.Activity
import android.util.Log
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.modules.base.MainMVP


class DownloadDataPresenter(private val view: View, val activity: Activity? = null) : MainMVP.Presenter,
    ApiEventsListeners.GetUsersListener {


    private val firebaseApiManager: FirebaseApiManager by lazy {
        FirebaseApiManager(
            onGetUsersListener = this,
            activity = activity!!
        )
    }


    fun downloadData() {
        view.showProgress(true)
        firebaseApiManager.getAllUsers()
    }



    override fun areUsersSaved(success: Boolean, users: ArrayList<UserResponse>) {
        view.showProgress(false)

        if (users.isNotEmpty()) {
            view.setUsersList(users)
            view.navigateToHome()

        } else {

        }

    }


    interface View : MainMVP.View {
        fun setUsersList(response: ArrayList<UserResponse>)
        fun showProgress(enable: Boolean)
        fun navigateToHome()
    }


}
