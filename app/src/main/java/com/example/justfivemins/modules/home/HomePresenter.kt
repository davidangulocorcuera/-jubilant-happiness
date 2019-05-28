package com.example.justfivemins.modules.home

import android.app.Activity
import android.util.Log
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.MainMVP

class HomePresenter(private val view: View, val activity: Activity? = null) : MainMVP.Presenter, ApiEventsListeners.UserDataListener,ApiEventsListeners.OnDataChangedListener, ApiEventsListeners.GetUsersListener {


    override fun areUsersSaved(success: Boolean, users: ArrayList<UserResponse>) {
        view.setUsersList(users)
        view.loadHome()
    }

    override fun isUserDataChanged(success: Boolean, userResponse: UserResponse) {
        if (success) {
            setCurrentUserData(userResponse)
        } else {

        }
    }

    private var currentUser: User = User()



    override fun isUserDataSaved(success: Boolean, userResponse: UserResponse) {
        if (success) {
            setCurrentUserData(userResponse)

        } else {
            Log.v("taag", "fail getting firebaseUser data")
        }
        view.showProgress(false)
    }

    fun downloadData(){
        val firebaseApiManager: FirebaseApiManager by lazy {
            FirebaseApiManager(
                userDataListener = this,
                onGetUsersListener = this,
                activity = activity!!
            )
        }
        firebaseApiManager.getUserData(CurrentUser.firebaseUser!!)
        firebaseApiManager.onUserDataChanged(CurrentUser.firebaseUser!!.uid)
        firebaseApiManager.getAllUsers()
    }

    fun setCurrentUserData(userResponse: UserResponse) {
        currentUser.name = userResponse.name
        currentUser.email = userResponse.email
        currentUser.birthday = userResponse.birthday
        currentUser.currentLocation = userResponse.location
        currentUser.age = userResponse.age

        currentUser.surname = userResponse.surname
        currentUser.jobName = userResponse.job
        currentUser.universityName = userResponse.university
        currentUser.description = userResponse.description
        currentUser.profileImageUrl = userResponse.profileImageUrl

        CurrentUser.user = currentUser
        view.showProgress(false)
        view.setMenuData(currentUser)
    }

    fun init() {
        view.showProgress(true)
    }

    interface View : MainMVP.View {
        fun showProgress(enable: Boolean)
        fun setMenuData(user: User)
        fun navigateToLocationFragment()
        fun loadHome()
        fun setUsersList(response: ArrayList<UserResponse>)
    }
}