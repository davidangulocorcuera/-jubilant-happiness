package com.example.justfivemins.modules.profile_data


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
        btnNext.setOnClickListener {
            showProgress(show = true, hasShade = true)
            disableScreenOnUpdate(false)
            updateUser(retrieveRegisterData())
        }
        setToolbarTitle(getString(R.string.my_data_title))

        val fields = arrayListOf<EditText>(etName, etDescription, etJob, etSurname, etUniversity)

        fields.forEach {
            it.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    it.postDelayed({

                        if (it == etName) {
                            presenter.isValidName(p0.toString())
                        }
                        if (it == etDescription) {
                            presenter.isValidDescription(p0.toString())
                        }
                        if (it == etSurname) {
                            presenter.isValidSurname(p0.toString())
                        }
                        if (it == etUniversity) {
                            presenter.isValidUniversity(p0.toString())
                        }
                        if (it == etJob) {
                            presenter.isValidJob(p0.toString())
                        }


                        presenter.onChange(retrieveRegisterData())
                    }, 400)

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })
        }

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


    override fun showNameError(error: Boolean) {
        if (error) {
            tiName.error = "this field can´t be empty"
        }

        tiName.showError(error)
    }

    override fun showSurnameError(error: Boolean) {
        if (error) {
            tiSurname.error = "this field can´t be empty"
        }

        tiSurname.showError(error)    }

    override fun showDescriptionError(error: Boolean) {
        if (error) {
            tiDescription.error = "this field can´t be empty"
        }

        tiDescription.showError(error)    }

    override fun showUniversityError(error: Boolean) {
        if (error) {
            tiUniversity.error = "this field can´t be empty"
        }

        tiUniversity.showError(error)    }

    override fun showJobError(error: Boolean) {
        if (error) {
            tiJob.error = "this field can´t be empty"
        }

        tiJob.showError(error)    }


    override fun enableEdit(enable: Boolean) {
        btnNext.isEnabled = enable
    }

    override fun hideInputErrors() {
        tiSurname.isErrorEnabled = false
        tiUniversity.isErrorEnabled = false
        tiDescription.isErrorEnabled = false
        tiName.isErrorEnabled = false
        tiJob.isErrorEnabled = false
    }
    private fun disableScreenOnUpdate(enable: Boolean) {
        tiName.isEnabled = enable
        btnNext.isEnabled = enable
        btnBack.isEnabled = enable
        tiJob.isEnabled = enable
        tiDescription.isEnabled = enable
        tiSurname.isEnabled = enable
        tiUniversity.isEnabled = enable
    }
    override fun isUserUpdated(success: Boolean) {
        showProgress(show = false, hasShade = false)
        disableScreenOnUpdate(true)
        if (success) {
            navigator.addBackStack(false).finishCurrent(true).navigateToHome()
        } else {
            Toast.makeText(
                activity?.applicationContext, "Register failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateUser(data: UpdateUserRequest) {
        showProgress(show = false, hasShade = false)
        val firebaseApiManager: FirebaseApiManager by lazy {
            FirebaseApiManager(
                updateUserListener = this,
                activity = activity!!
            )
        }
        return firebaseApiManager.updateUserData(data, CurrentUser.firebaseUser!!.uid)
    }

}