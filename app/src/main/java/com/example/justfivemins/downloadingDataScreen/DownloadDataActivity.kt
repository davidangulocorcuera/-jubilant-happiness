package com.example.justfivemins.downloadingDataScreen

import android.os.Bundle
import com.example.justfivemins.R
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseActivity



class DownloadDataActivity : BaseActivity(), DownloadDataPresenter.View {
    private val users = ArrayList<User>()
    private val presenter: DownloadDataPresenter by lazy { DownloadDataPresenter(this,this) }
    private var user  = User()



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


        navigator.addExtra("users", users)
            .addExtra("currentUser", user)
            .addBackStack(false)
            .finishCurrent(true)
            .navigateToHome()





    }
}
