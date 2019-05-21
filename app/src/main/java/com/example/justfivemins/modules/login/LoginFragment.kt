package com.example.justfivemins.modules.login

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.example.justfivemins.R
import com.example.justfivemins.firebase.FirebaseApiManager
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.utils.showError
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment(),LoginFragmentPresenter.View {


    private val presenter: LoginFragmentPresenter by lazy { LoginFragmentPresenter(this) }

    override fun onCreateViewId(): Int {
        return R.layout.fragment_login
    }

    override fun viewCreated(view: View?) {
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
    private fun retrieveLoginData(): LoginRequest{
        val login = LoginRequest()
        login.email = etEmail?.text.toString()
        login.password = etPassword?.text.toString()
        return login

    }
    private fun setButtonsListeners(){
        btnLogin.setOnClickListener {
            showProgress(show = true, hasShade = true)
            signUser(retrieveLoginData())
            goToHome()
        }
        btnRegister.setOnClickListener {
            gotToRegister()
        }
    }
    private fun signUser(data: LoginRequest) {
        val firebaseApiManager = FirebaseApiManager()
        return firebaseApiManager.loginUser(
            data, activity!!
        )
    }

    override fun goToHome() {
        navigator.let {
            it.finishCurrent(true)
            it.addBackStack(true).navigateToHome()
        }

    }

    override fun gotToRegister() {
        navigator.addBackStack(true).navigateToRegister()
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
    override fun onResume() {
        super.onResume()
        showProgress(show = false, hasShade = false)

    }

    override fun onDestroy() {
        super.onDestroy()
        showProgress(show = false, hasShade = false)
    }
}
