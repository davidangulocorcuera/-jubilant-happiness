package com.example.justfivemins.modules.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.justfivemins.R
import com.example.justfivemins.api.Api
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.requests.RegisterRequest
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.utils.DatePickerFragment
import com.example.justfivemins.utils.DateUtils
import com.example.justfivemins.utils.DateUtils.DATE_FORMAT_USER
import com.example.justfivemins.utils.showError
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*


class RegisterFragment : BaseFragment(), RegisterPresenter.View, ApiEventsListeners.RegisterListener {

    companion object {
        var registerRequest: RegisterRequest =
            RegisterRequest()
    }

    private var age: Int = 0
    private lateinit var api: Api
    private val presenter: RegisterPresenter by lazy { RegisterPresenter(this) }
    private val firebaseApiManager: FirebaseApiManager by lazy { FirebaseApiManager(registerListener = this) }


    override fun onCreateViewId(): Int {
        return R.layout.fragment_register
    }

    override fun viewCreated(view: View?) {
        btnPrivacyTerms.setOnClickListener {
        }
        ivOpenCalendar.setOnClickListener {
            showDatePickerDialog()
        }
        etName.setOnLongClickListener {
            autoFill()
            true
        }
        setToolbarTitle(getString(R.string.register))
        setListeners()
        val fields = arrayListOf<EditText>(etEmail, etPassword, etPasswordRepeat, etName, etBirthday)

        fields.forEach {
            it.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    it.postDelayed({

                        if (it == etEmail) {
                            presenter.isValidEmail(p0.toString())
                        }
                        if (it == etName) {
                            presenter.isValidName(p0.toString())
                        }

                        if (it == etPassword) {
                            presenter.isValidPassword(p0.toString(), etPasswordRepeat.text.toString())
                        }

                        if (it == etPasswordRepeat) {
                            presenter.isValidPassword(etPassword.text.toString(), p0.toString())
                        }
                        if (it == etBirthday) {
                            presenter.isValidBirthday(p0.toString())
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
        return firebaseApiManager.createUser(
            data,
            data.password,
            activity!!
        )
    }


    private fun retrieveRegisterData(): RegisterRequest {
        val register = RegisterRequest()
        register.email = etEmail.text.toString()
        register.password = etPassword.text.toString()
        register.confirmPassword = etPasswordRepeat?.text.toString()
        register.name = etName.text.toString()
        register.birthday = etBirthday.text.toString()
        register.age = age
        return register
    }

    private fun autoFill() {
        etEmail?.setText("ce@g.com")
        etPassword.setText("Berlinwood1")
        etPasswordRepeat.setText("Berlinwood1")
        etName.setText("dvd")
    }

    private fun setListeners() {
        btnNext.setOnClickListener {
            showProgress(show = true, hasShade = true)
            disableScreenOnRegister(false)
            registerUser(retrieveRegisterData())
        }
        btnBack.setOnClickListener {
            backToLogin()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment()
        newFragment.arguments = Bundle()
        newFragment.onDateSelected {
        age = getAge(it.get(Calendar.YEAR),it.get(Calendar.MONTH),it.get(Calendar.DAY_OF_MONTH))
            etBirthday.setText(DateUtils.formatTarget(DATE_FORMAT_USER).formatDate(it))
        }
        newFragment.show(this.activity?.supportFragmentManager, "")

    }

    private fun getAge(year: Int, month: Int, day: Int): Int {
        var age = Calendar.getInstance().get(Calendar.YEAR) - year
        if((Calendar.getInstance().get(Calendar.MONTH) < month) ||
            (Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < day
                    )) age--
        return age
    }


    private fun disableScreenOnRegister(enable: Boolean) {
        tiName.isEnabled = enable
        tiBirthday.isEnabled = enable
        tiPasswordRepeat.isEnabled = enable
        tiPassword.isEnabled = enable
        tiEmail.isEnabled = enable
        btnNext.isEnabled = enable
        btnBack.isEnabled = enable
    }

    private fun goToNextScreen() {
        navigator.addBackStack(false).navigateToRequestLocationDialog()
    }

    private fun backToLogin() {
        navigator.navigateToLogin()
    }

    override fun hideInputErrors() {
        tiEmail.isErrorEnabled = false
        tiPassword.isErrorEnabled = false
        tiPasswordRepeat.isErrorEnabled = false
        tiBirthday.isErrorEnabled = false
        tiName.isErrorEnabled = false
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

    override fun showNameError(error: Boolean) {
        if (error) {
            tiName.error = "Invalid name or empty"
        }

        tiName.showError(error)
    }


    override fun showBirthdayError(error: Boolean) {
        if (error && age < 18) tiBirthday.error = "Invalid date or empty"
        tiBirthday.showError(error)
    }

    override fun isRegister(success: Boolean) {
        showProgress(show = false, hasShade = false)
        disableScreenOnRegister(true)

        if (success) {
            goToNextScreen()
        } else {
            Toast.makeText(
                activity?.applicationContext, "Register failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}
