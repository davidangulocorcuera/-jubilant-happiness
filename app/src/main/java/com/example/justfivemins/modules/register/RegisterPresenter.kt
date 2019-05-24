package com.example.justfivemins.modules.register

import android.os.Bundle
import com.example.justfivemins.modules.base.MainMVP
import com.example.justfivemins.api.requests.RegisterRequest
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

    fun isValidBirthday(text: String, age: Int) {
        view.showBirthdayError(!Valid.isNotNullOrEmpty(text))
        if(age < 18){
            view.showBirthdayError(true)
        }
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
            view.showNameError(!Valid.isNotNullOrEmpty(registerRequest.name))

        if (!Valid.isEmailValid(registerRequest.email)) {
            error = true
        }
        if (showError)
            view.showEmailError(!Valid.isNotNullOrEmpty(registerRequest.email))

        if (!Valid.isPasswordValid(registerRequest.password, registerRequest.confirmPassword)) {
            error = true
        }
        if (showError)
            view.showPasswordError(!Valid.isPasswordValid(registerRequest.password, registerRequest.confirmPassword))

        if (!Valid.isNotNullOrEmpty(registerRequest.birthday) || registerRequest.age < 18) {
            error = true
        }
        if (showError) {
            view.showBirthdayError(!Valid.isNotNullOrEmpty(registerRequest.birthday))
        }


        return error
    }

    interface View : MainMVP.View {

        fun hideInputErrors()
        fun showNameError(error: Boolean)
        fun showEmailError(error: Boolean)
        fun showPasswordError(error: Boolean)
        fun enableRegister(enable: Boolean)
        fun showPasswordConfirmError(error: Boolean)
        fun showBirthdayError(error: Boolean)

    }
}