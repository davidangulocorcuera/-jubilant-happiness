package com.example.justfivemins.modules.register

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.example.justfivemins.R
import com.example.justfivemins.firebase.Api
import com.example.justfivemins.firebase.FirebaseApiManager
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.requests.RegisterRequest
import com.example.justfivemins.utils.showError
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment(), RegisterPresenter.View {
    companion object {
        var registerRequest: RegisterRequest =
            RegisterRequest()
    }

    private lateinit var api: Api
    private val presenter: RegisterPresenter by lazy { RegisterPresenter(this) }

    override fun onCreateViewId(): Int {
        return R.layout.fragment_register
    }

    override fun viewCreated(view: View?) {
        tiName.setOnLongClickListener {
            autoFill()
            true
        }
        setToolbarTitle(getString(R.string.register))
        setListeners()
        val fields = arrayListOf<EditText>(etEmail, etPassword, etPasswordRepeat)

        fields.forEach {
            it.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    it.postDelayed({


                        if (it == etEmail) {
                            presenter.isValidEmail(p0.toString())
                        }

                        if (it == etPassword) {
                            presenter.isValidPassword(p0.toString(), etPasswordRepeat.text.toString())
                        }

                        if (it == etPasswordRepeat) {
                            presenter.isValidPassword(etPassword.text.toString(), p0.toString())
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

    private fun registerUser(data: RegisterRequest) {
        val firebaseApiManager = FirebaseApiManager()
        return firebaseApiManager.createUser(
            data,
            data.password,
            activity!!
        )


        /*  registerRequest.let {
              data.email?.let { email -> data.password?.let { password ->
                  activity?.let { activity ->
                      firebaseApiManager.createUser(email,
                          password, activity
                      )
                  }
              } }
          }*/

    }


    private fun retrieveRegisterData(): RegisterRequest {
        val register = RegisterRequest()
        register.email = etEmail?.text.toString()
        register.password = etPassword?.text.toString()
        register.confirmPassword = etPasswordRepeat?.text.toString()
        register.name = etSurname?.text.toString()
        register.surname = etSurname?.text.toString()
        return register
    }

    private fun autoFill() {
        etEmail?.setText("ce@g.com")
        etPassword.setText("Berlinwood1")
        etPasswordRepeat.setText("Berlinwood1")
        etName.setText("dvd")
        etSurname.setText("cebollo")
    }

    private fun setListeners() {
        btnNext.setOnClickListener {
            showProgress(show = true, hasShade = true)
            registerUser(retrieveRegisterData())
            goToNextScreen()

        }
        btnBack.setOnClickListener {
            backToLogin()
        }
    }

    override fun onResume() {
        super.onResume()
        showProgress(show = false, hasShade = false)

    }

    override fun onDestroy() {
        super.onDestroy()
        showProgress(show = false, hasShade = false)
    }


    fun goToNextScreen() {
        navigator.navigateToHome()
    }

    fun backToLogin() {
        navigator.navigateToLogin()
    }

    override fun hideInputErrors() {
        tiEmail.isErrorEnabled = false
        tiPassword.isErrorEnabled = false
        tiPasswordRepeat.isErrorEnabled = false

        tiPassword.isErrorEnabled = false
        tiPasswordRepeat.passwordVisibilityToggleDrawable
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
        btnNext.isEnabled = enable
    }


    override fun showPasswordConfirmError(error: Boolean) {
        if (error) {
            tiPasswordRepeat.error = "Invalid Password or empty"
        }
        tiPasswordRepeat.showError(error)
    }

    override fun showSurnameError(error: Boolean) {
        if (error) {
            tiName.error = "Invalid name or empty"
        }

        tiName.showError(error)
    }

    override fun showNameError(error: Boolean) {
        if (error) {
            tiSurname.error = "Invalid surname or empty"
        }

        tiSurname.showError(error)
    }


}
