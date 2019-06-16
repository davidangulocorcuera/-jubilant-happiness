package com.example.justfivemins.modules.downloadingDataScreen

import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.justfivemins.R
import com.example.justfivemins.api.responses.UserResponse
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.home.MainViewModel

/**  Fragment for get all the necesary data from the bbdd for run the application */


class DownloadDataFragment : BaseFragment(), DownloadDataPresenter.View {


    private val users = ArrayList<User>()
    private val presenter: DownloadDataPresenter by lazy { DownloadDataPresenter(this, activity) }
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(activity!!).get(MainViewModel()::class.java)
    }


    override fun setUsersList(response: ArrayList<UserResponse>) {
        var unknownUser = User()
        response.forEach { userResponse ->
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
            unknownUser.id = userResponse.id

            if(userResponse.id != CurrentUser.firebaseUser?.uid){
                users.add(unknownUser)
            }
            unknownUser = User()
        }
        mainViewModel.users.postValue(users)
    }

    override fun showProgress(enable: Boolean) {
        showProgress(enable, enable)
    }


    override fun onCreateViewId(): Int {
        return R.layout.fragment_download_data
    }

    override fun viewCreated(view: View?) {
        presenter.downloadData()
    }


    override fun navigateToHome(user: User) {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.goToHomeFragment)
        }

    }
}

