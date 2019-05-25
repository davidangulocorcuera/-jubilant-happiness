package com.example.justfivemins.modules.profile_data

import com.example.justfivemins.api.requests.UpdateUserRequest
import com.example.justfivemins.modules.base.MainMVP
import com.example.justfivemins.utils.Valid

class ProfileDataPresenter(private val view: View) {

    fun isValidName(text: String) {
        view.showNameError(!Valid.isNotNullOrEmpty(text))
    }
    fun isValidSurname(text: String) {
        view.showSurnameError(!Valid.isNotNullOrEmpty(text))
    }

    fun isValidJob(text: String) {
        view.showJobError(!Valid.isNotNullOrEmpty(text))
    }
    fun isValidUniversity(text: String) {
        view.showUniversityError(!Valid.isNotNullOrEmpty(text))
    }
    fun isValidDescription(text: String){
        view.showDescriptionError(!Valid.isNotNullOrEmpty(text))
    }


    fun validate(showError: Boolean = true, updateUserRequest: UpdateUserRequest): Boolean {
        if (showError) view.hideInputErrors()
        var error = false
        if (!Valid.isNotNullOrEmpty(updateUserRequest.name)) {
            error = true
        }
        if (showError)
            view.showNameError(!Valid.isNotNullOrEmpty(updateUserRequest.name))


        if (!Valid.isNotNullOrEmpty(updateUserRequest.surname)) {
            error = true
        }
        if (showError)
            view.showSurnameError(!Valid.isNotNullOrEmpty(updateUserRequest.surname))

        if (!Valid.isNotNullOrEmpty(updateUserRequest.university)) {
            error = true
        }
        if (showError)
            view.showUniversityError(!Valid.isNotNullOrEmpty(updateUserRequest.university))

        if (!Valid.isNotNullOrEmpty(updateUserRequest.job)) {
            error = true
        }
        if (showError)
            view.showJobError(!Valid.isNotNullOrEmpty(updateUserRequest.job))

        if (!Valid.isNotNullOrEmpty(updateUserRequest.description)) {
            error = true
        }
        if (showError)
            view.showDescriptionError(!Valid.isNotNullOrEmpty(updateUserRequest.description))

        return error
    }

    fun onChange(updateUserRequest: UpdateUserRequest) {
        val error = validate(false, updateUserRequest)
        if (!error) {
            view.enableEdit(true)
        } else {
            view.enableEdit(false)
        }
    }
    interface View : MainMVP.View {
        fun showNameError(error: Boolean)
        fun showSurnameError(error: Boolean)
        fun showDescriptionError(error: Boolean)
        fun showUniversityError(error: Boolean)
        fun showJobError(error: Boolean)
        fun enableEdit(enable: Boolean)
        fun hideInputErrors()
    }

}