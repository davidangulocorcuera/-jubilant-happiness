package com.example.justfivemins.modules.login

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.requests.LoginRequest
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.utils.showError
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment(), LoginFragmentPresenter.View, ApiEventsListeners.LoginListener {

    private val presenter: LoginFragmentPresenter by lazy { LoginFragmentPresenter(this) }


    override fun isLogged(success: Boolean){
        showProgress(show = false, hasShade = false)
        if (success) {
                navigator.finishCurrent(true).navigateToRequestLocationDialog()
        } else {
            Toast.makeText(
                activity?.applicationContext, "Authentication failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

 fun autofill(){
     etEmail.setText("ce@g.com")
     etPassword.setText("Berlinwood1")
 }
    override fun onCreateViewId(): Int {
        return R.layout.fragment_login
    }

    override fun viewCreated(view: View?) {
        etEmail.setOnLongClickListener{
            autofill()
            true
        }
        hideToolbar()
        presenter.init()
        setButtonsListeners()
        val fields = arrayListOf<EditText>(etEmail, etPassword)

        fields.forEach {
            it.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    it.postDelayed({


                        if (it == etEmail) {
                            presenter.isValidEmail(p0.toString())
                        }

                        if (it == etPassword) {
                            presenter.isValidPassword(p0.toString())
                        }


                        presenter.onChange(retrieveLoginData())
                    }, 400)

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })
        }
    }

    private fun retrieveLoginData(): LoginRequest {
        val login = LoginRequest()
        login.email = etEmail?.text.toString()
        login.password = etPassword?.text.toString()
        return login

    }

    private fun setButtonsListeners() {
        btnLogin.setOnClickListener {
            showProgress(show = true, hasShade = true)
            signUser(retrieveLoginData())


        }
        btnRegister.setOnClickListener {
            gotToRegister()
        }
    }

    private fun signUser(data: LoginRequest) {
        val firebaseApiManager: FirebaseApiManager by lazy { FirebaseApiManager(this,activity = activity!!) }
        return firebaseApiManager.loginUser(data)
    }

    override fun goToHome() {
        navigator.let {
            it.finishCurrent(true)
            it.addBackStack(true).navigateToHome()
        }

    }

    override fun gotToRegister() {
        navigator.addBackStack(true).finishCurrent(true).navigateToRegister()
    }

    override fun showEmailError(error: Boolean) {
        if (error) {
            tiEmail.error = "Invalid e-mail or empty"
        }

        tiEmail.showError(error)
    }

    override fun showPasswordError(error: Boolean) {
        if (error) {
            tiPassword.error = "Invalid Password or empty"
        }
        tiPassword.showError(error)
    }

    override fun enableRegister(enable: Boolean) {
        btnLogin.isEnabled = enable
    }

    override fun hideInputErrors() {
        tiEmail.isErrorEnabled = false
        tiPassword.isErrorEnabled = false

    }


}
