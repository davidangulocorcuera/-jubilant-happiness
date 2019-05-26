package com.example.justfivemins.modules.profile_data


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.requests.UpdateUserRequest
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.utils.showError
import kotlinx.android.synthetic.main.fragment_profile_data.*

class ProfileDataFragment : BaseFragment(), ProfileDataPresenter.View, ApiEventsListeners.UpdateUserListener{

    private val presenter: ProfileDataPresenter by lazy { ProfileDataPresenter(this) }


    override fun onCreateViewId(): Int {
        return R.layout.fragment_profile_data
    }

    override fun viewCreated(view: View?) {
        enableDrawerMenu(false)
        hasToolbarBackButton()
        btnNext.setOnClickListener {
            updateUser(retrieveRegisterData())
        }
        setToolbarTitle(getString(R.string.my_data_title))


    }

    override fun onDestroy() {
        enableDrawerMenu(true)
        super.onDestroy()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        etName.setText(CurrentUser.user?.name)
        etSurname.setText(CurrentUser.user?.surname)
        etUniversity.setText(CurrentUser.user?.universityName)
        etJob.setText(CurrentUser.user?.jobName)
        etDescription.setText(CurrentUser.user?.description)


    }

    private fun retrieveRegisterData(): UpdateUserRequest {
        val updateUserRequest = UpdateUserRequest()
        updateUserRequest.name = etName.text.toString()
        updateUserRequest.surname = etSurname.text.toString()
        updateUserRequest.description = etDescription.text.toString()
        updateUserRequest.university = etUniversity.text.toString()
        updateUserRequest.job = etJob.text.toString()
        return updateUserRequest
    }



    private fun enableScreenOnUpdate(enable: Boolean) {
        tiName.isEnabled = enable
        btnNext.isEnabled = enable
        tiJob.isEnabled = enable
        tiDescription.isEnabled = enable
        tiSurname.isEnabled = enable
        tiUniversity.isEnabled = enable
    }
    override fun isUserUpdated(success: Boolean) {
        showProgress(show = false, hasShade = false)
        enableScreenOnUpdate(true)
        if (success) {

        } else {
            Toast.makeText(
                activity?.applicationContext, "Register failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUser(data: UpdateUserRequest) {
        enableScreenOnUpdate(false)
        showProgress(show = true, hasShade = true)
        val firebaseApiManager: FirebaseApiManager by lazy {
            FirebaseApiManager(
                updateUserListener = this,
                activity = activity!!
            )
        }
        return firebaseApiManager.updateUserData(data, CurrentUser.firebaseUser!!.uid)
    }

}