package com.example.justfivemins.modules.downloadingDataScreen

import android.app.Activity
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.MainMVP


class DownloadDataPresenter(private val view: View, val activity: Activity? = null) : MainMVP.Presenter,
    ApiEventsListeners.GetUsersListener, ApiEventsListeners.UserDataListener {

    private val firebaseApiManager: FirebaseApiManager by lazy {
        FirebaseApiManager(
            onGetUsersListener = this,
            userDataListener = this,
            activity = activity!!
        )
    }

    private lateinit var user: User


    fun downloadData() {
        view.showProgress(true)
        firebaseApiManager.getUserData(CurrentUser.firebaseUser!!)
    }


    override fun areUsersSaved(success: Boolean, users: ArrayList<UserResponse>) {
        view.showProgress(false)

        if (users.isNotEmpty()) {
            view.setUsersList(users)
            view.navigateToHome(user)
        } else {

        }

    }

    override fun isUserDataSaved(success: Boolean, userResponse: UserResponse) {
        if (success) {
            setUserData(userResponse)
            firebaseApiManager.getAllUsers()

        }
    }

    fun setUserData(userResponse: UserResponse) {
        user = User()
        user.profileImageUrl = userResponse.profileImageUrl
        user.age = userResponse.age
        user.birthday = userResponse.birthday
        user.currentLocation = userResponse.location
        user.email = userResponse.email
        user.name = userResponse.name
        user.description = userResponse.description
        user.gender = userResponse.gender
        user.jobName = userResponse.name

        CurrentUser.user = user
    }

    interface View : MainMVP.View {
        fun setUsersList(response: ArrayList<UserResponse>)
        fun showProgress(enable: Boolean)
        fun navigateToHome(user: User)
    }


}
