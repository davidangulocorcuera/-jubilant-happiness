package com.example.justfivemins.modules.login


import com.example.justfivemins.api.requests.LoginRequest
import com.example.justfivemins.modules.base.MainMVP
import com.example.justfivemins.utils.Valid

class LoginPresenter(private val view: View) : MainMVP.Presenter {

    fun onChange(loginRequest: LoginRequest) {
        val error = validate(false, loginRequest)
        if (!error) {
            view.enableRegister(true)
        } else {
            view.enableRegister(false)
        }
    }
    fun isValidEmail(text: String) {
        view.showEmailError(!Valid.isEmailValid(text))
    }

    fun isValidPassword(password: String) {
        view.showPasswordError(!Valid.isPasswordValid(password))
    }

    fun validate(showError: Boolean = true, loginRequest: LoginRequest): Boolean {
        if (showError) view.hideInputErrors()
        var error = false

        if (!Valid.isEmailValid(loginRequest.email)) {
            error = true
        }
        if (showError)
            view.showEmailError(!Valid.isNotNullOrEmpty(loginRequest.email))

        if (!Valid.isNotNullOrEmpty(loginRequest.password)) {
            error = true
        }
        if (showError)
            view.showPasswordError(!Valid.isNotNullOrEmpty(loginRequest.password))
        return error
    }

    interface View : MainMVP.View {
        fun goToRegister()
        fun showEmailError(error: Boolean)
        fun showPasswordError(error: Boolean)
        fun enableRegister(enable: Boolean)
        fun hideInputErrors()

    }
}