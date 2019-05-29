package com.example.justfivemins.downloadingDataScreen

import android.os.Bundle
import android.util.Log
import com.example.justfivemins.R
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseActivity
import java.util.*
import kotlin.collections.ArrayList


class DownloadDataActivity : BaseActivity(), DownloadDataPresenter.View {
    private val users = ArrayList<User>()
    private val presenter: DownloadDataPresenter by lazy { DownloadDataPresenter(this,this) }
    private var currentUser  = User()




    override fun setUsersList(response: ArrayList<UserResponse>) {
        var unknownUser: User = User()

        response.forEach {userResponse ->
            unknownUser.name = userResponse.name
            unknownUser.email = userResponse.email
            unknownUser.birthday = userResponse.birthday
            unknownUser.currentLocation = userResponse.location
            unknownUser.age = userResponse.age

            unknownUser.surname = userResponse.surname
            unknownUser.jobName = userResponse.job
            unknownUser.universityName = userResponse.university
            unknownUser.description = userResponse.description
            unknownUser.profileImageUrl = userResponse.profileImageUrl


            users.add(unknownUser)
            unknownUser = User()
        }


    }
    override fun showProgress(enable:Boolean){
        showProgress(enable,enable)
    }


    override fun onCreateViewId(): Int {
        return R.layout.activity_splash
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.downloadData()

    }
   override fun navigateToHome(){
       if(users.isNotEmpty()){
           navigator.addExtra("users", users)
               .addExtra("currentUser", currentUser)
               .navigateToHome()
       }
    }
    override fun setCurrentUserData(userResponse: UserResponse) {
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

        Log.v("taag", "currentUser  in setUser from dwdactv" + currentUser.name)


        CurrentUser.user = currentUser
    }

}
