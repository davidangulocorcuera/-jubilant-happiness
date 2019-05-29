package com.example.justfivemins.downloadingDataScreen

import android.app.Activity
import android.util.Log
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.modules.base.MainMVP


class DownloadDataPresenter(private val view: View, val activity: Activity? = null) : MainMVP.Presenter,
    ApiEventsListeners.UserDataListener,
    ApiEventsListeners.GetUsersListener {


    private val firebaseApiManager: FirebaseApiManager by lazy {
        FirebaseApiManager(
            userDataListener = this,
            onGetUsersListener = this,
            activity = activity!!
        )
    }


    fun downloadData() {
        view.showProgress(true)

        firebaseApiManager.getUserData(CurrentUser.firebaseUser!!)
        firebaseApiManager.onUserDataChanged(CurrentUser.firebaseUser!!.uid)


    }


    override fun isUserDataSaved(success: Boolean, userResponse: UserResponse) {

        if (success) {
            view.setCurrentUserData(userResponse)
            firebaseApiManager.getAllUsers()
            view.navigateToHome()

        } else {
            Log.v("taag", "fail getting firebaseUser data")

        }
    }


    override fun areUsersSaved(success: Boolean, users: ArrayList<UserResponse>) {
        Log.v("taag", "areUsersSaved" + users.size)
        if (users.isNotEmpty()) {
            view.setUsersList(users)
            view.showProgress(false)
            view.navigateToHome()


        } else {

        }

    }


    interface View : MainMVP.View {
        fun setUsersList(response: ArrayList<UserResponse>)
        fun showProgress(enable: Boolean)
        fun navigateToHome()
        fun setCurrentUserData(userResponse: UserResponse)
    }


}
