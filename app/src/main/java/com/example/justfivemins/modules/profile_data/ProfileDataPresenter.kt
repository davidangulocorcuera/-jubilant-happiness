package com.example.justfivemins.modules.profile_data

import com.example.justfivemins.api.requests.UpdateUserRequest
import com.example.justfivemins.modules.base.MainMVP
import com.example.justfivemins.utils.Valid

class ProfileDataPresenter(private val view: View) {

    fun validate(showError: Boolean = true, updateUserRequest: UpdateUserRequest): Boolean {
        if (showError) view.hideInputErrors()
        var error = false
        if (!Valid.isNotNullOrEmpty(updateUserRequest.name)) {
            error = true
        }
        if (showError)
            view.showNameError(!Valid.isNotNullOrEmpty(updateUserRequest.name))
        return error
    }

    fun isValidName(text: String) {
        view.showNameError(!Valid.isNotNullOrEmpty(text))
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
        fun enableEdit(enable: Boolean)
        fun hideInputErrors()

    }

}