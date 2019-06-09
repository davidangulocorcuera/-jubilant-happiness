package com.example.justfivemins.modules.login

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.requests.LoginRequest
import com.example.justfivemins.model.CurrentUser
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.utils.showError
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import org.aviran.cookiebar2.CookieBar
import android.widget.Toast
import com.example.justfivemins.utils.Valid
import com.yarolegovich.lovelydialog.LovelyTextInputDialog


class LoginFragment : BaseFragment(), LoginFragmentPresenter.View, ApiEventsListeners.LoginListener,
    ApiEventsListeners.OnResetPasswordEmailSentListener {
    override fun isEmailSent(success: Boolean) {
        Toast.makeText(
            context,
            "email enviado",
            Toast.LENGTH_SHORT
        ).show()
    }

    private val presenter: LoginFragmentPresenter by lazy { LoginFragmentPresenter(this) }


    override fun isLogged(success: Boolean) {
        showProgress(show = false, hasShade = false)
        if (success) {
            goToDownloadData()
        } else {
            CookieBar.build(activity)
                .setCookiePosition(CookieBar.BOTTOM)
                .setAction("CLOSE") {
                    enableScreen(true)
                    CookieBar.dismiss(activity)
                }

                .setSwipeToDismiss(false)
                .setEnableAutoDismiss(false)
                .setTitle(getString(R.string.invalid_access))
                .setBackgroundColor(R.color.materialRed800)
                .setMessage(getString(R.string.login_error_info))
                .show()
        }
    }

    private fun goToDownloadData() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.goToDownloadFragment)
        }
    }

    private fun autofill() {
        etEmail.setText("ce@g.com")
        etPassword.setText("Berlinwood1")
    }

    private fun retrieveLoginData(): LoginRequest {
        val login = LoginRequest()
        login.email = etEmail?.text.toString()
        login.password = etPassword?.text.toString()
        return login

    }

    private fun setButtonsListeners() {
        btnLogin.setOnClickListener {
            enableScreen(false)
            showProgress(show = true, hasShade = true)
            signUser(retrieveLoginData())


        }
        btnRegister.setOnClickListener {
            goToRegister()
        }
    }

    private fun signUser(data: LoginRequest) {
        val firebaseApiManager: FirebaseApiManager by lazy { FirebaseApiManager(this, activity = activity!!) }
        return firebaseApiManager.loginUser(data)
    }

    private fun enableScreen(enable: Boolean) {
        tiPassword.isEnabled = enable
        tiEmail.isEnabled = enable
        btnLogin.isEnabled = enable
        btnRegister.isEnabled = enable
        tvResetPassword.isEnabled = enable
    }


    override fun onCreateViewId(): Int {
        return R.layout.fragment_login
    }

    private fun checkUserAuthState() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            CurrentUser.firebaseUser = user
            goToDownloadData()
        }
    }

    override fun viewCreated(view: View?) {
        checkUserAuthState()
        etEmail.setOnLongClickListener {
            autofill()
            true
        }
        tvResetPassword.setOnClickListener {
            showResetPasswordDialog()
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


    override fun goToRegister() {
        findNavController().navigate(R.id.goToRegister)
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
        btnLogin?.isEnabled = enable
    }

    override fun hideInputErrors() {
        tiEmail.isErrorEnabled = false
        tiPassword.isErrorEnabled = false

    }

    override fun onDestroy() {
        super.onDestroy()
        showProgress(false, false)
        CookieBar.dismiss(activity)
    }

    fun showResetPasswordDialog() {
        LovelyTextInputDialog(context, R.style.ThemeOverlay_MaterialComponents_TextInputEditText)
            .setTopColorRes(R.color.white)
            .setTitle(R.string.reset_password_title)
            .setMessage(getString(R.string.reset_password_info))
            .setIcon(R.drawable.ic_messages)
            .setInputFilter(R.string.email_error,
                LovelyTextInputDialog.TextFilter { text ->  Valid.isEmailValid(text)})
            .setConfirmButton(android.R.string.ok,
                LovelyTextInputDialog.OnTextInputConfirmListener { text ->
                    sendEmailForResetPassword(text)
                })
            .show()
    }

    fun sendEmailForResetPassword(email: String) {
        val firebaseApiManager: FirebaseApiManager by lazy {
            FirebaseApiManager(
                this,
                activity = activity!!,
                onResetPasswordEmailSentListener = this
            )
        }
        firebaseApiManager.sendPasswordEmail(email)
    }


}
