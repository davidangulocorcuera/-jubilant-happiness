package com.example.justfivemins.modules.register

import android.os.Bundle
import com.example.justfivemins.modules.base.MainMVP
import com.example.justfivemins.requests.RegisterRequest
import com.example.justfivemins.utils.Valid

class RegisterPresenter(private val view: RegisterPresenter.View) : MainMVP.Presenter {


    fun init(arguments: Bundle?) {


    }

    fun onChange(registerRequest: RegisterRequest) {
        val error = validate(false, registerRequest)
        if (!error) {
            view.enableRegister(true)
        } else {
            view.enableRegister(false)
        }
    }

    fun isValidName(text: String) {
        view.showNameError(!Valid.isNotNullOrEmpty(text))
    }

    fun isValidSurname(text: String) {
        view.showSurnameError(!Valid.isNotNullOrEmpty(text))
    }

    fun isValidEmail(text: String) {
        view.showEmailError(!Valid.isEmailValid(text))
    }

    fun isValidPassword(password: String, confirmPassword: String) {
        view.showPasswordError(!Valid.isPasswordValid(password, confirmPassword))
        view.showPasswordConfirmError(!Valid.isPasswordValid(password, confirmPassword))
    }

    fun validate(showError: Boolean = true, registerRequest: RegisterRequest): Boolean {
        if (showError) view.hideInputErrors()
        var error = false
        if (!Valid.isNotNullOrEmpty(registerRequest.name)) {
            error = true
        }
        if (showError)
            view.showNameError(!Valid.isNotNullOrEmpty(registerRequest.surname))

        if (!Valid.isNotNullOrEmpty(registerRequest.surname)) {
            error = true
        }
        if (showError)
            view.showSurnameError(!Valid.isNotNullOrEmpty(registerRequest.surname))

        if (!Valid.isEmailValid(registerRequest.email!!)) {
            error = true
        }
        if (showError)
            view.showEmailError(!Valid.isNotNullOrEmpty(registerRequest.email))

        if (!Valid.isPasswordValid(registerRequest.password!!, registerRequest.confirmPassword!!)) {
            error = true
        }
        if (showError)
            view.showPasswordError(!Valid.isPasswordValid(registerRequest.password!!, registerRequest.confirmPassword!!))


        return error
    }

    interface View : MainMVP.View {

        fun hideInputErrors()
        fun showNameError(error: Boolean)
        fun showEmailError(error: Boolean)
        fun showPasswordError(error: Boolean)
        fun enableRegister(enable: Boolean)
        fun showSurnameError(error: Boolean)
        fun showPasswordConfirmError(error: Boolean)
    }
}