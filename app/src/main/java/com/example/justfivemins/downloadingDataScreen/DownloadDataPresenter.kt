package com.example.justfivemins.downloadingDataScreen

import android.app.Activity
import android.util.Log
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.MainMVP
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class DownloadDataPresenter(private val view: View, val activity: Activity? = null) : MainMVP.Presenter,
    ApiEventsListeners.UserDataListener,
    ApiEventsListeners.GetUsersListener {
    private var currentUser: User = User()
    private val lock = ReentrantLock()
    private val condition: Condition = lock.newCondition()

    private val firebaseApiManager: FirebaseApiManager by lazy {
        FirebaseApiManager(
            userDataListener = this,
            onGetUsersListener = this,
            activity = activity!!
        )
    }


    fun downloadData() {
        lock.withLock {
            condition.await()
        }
        view.showProgress(true)
        firebaseApiManager.getUserData(CurrentUser.firebaseUser!!)
        firebaseApiManager.onUserDataChanged(CurrentUser.firebaseUser!!.uid)

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
    }

    override fun isUserDataSaved(success: Boolean, userResponse: UserResponse) {
        if (success) {
            setCurrentUserData(userResponse)
            firebaseApiManager.getAllUsers()
            view.navigateToHome()

        } else {
            Log.v("taag", "fail getting firebaseUser data")
            view.showProgress(false)

        }
    }


    override fun areUsersSaved(success: Boolean, users: ArrayList<UserResponse>) {

        if (users.isNotEmpty()) {
            view.setUsersList(users)
            view.showProgress(false)
            lock.withLock {
                condition.await()
            }
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