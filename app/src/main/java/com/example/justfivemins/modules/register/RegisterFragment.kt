package com.example.justfivemins.modules.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.navigation.Navigation
import com.example.justfivemins.R
import com.example.justfivemins.api.ApiEventsListeners
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.api.requests.RegisterRequest
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.utils.DatePickerFragment
import com.example.justfivemins.utils.DateUtils
import com.example.justfivemins.utils.DateUtils.DATE_FORMAT_USER
import com.example.justfivemins.utils.showError
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_register.*
import org.aviran.cookiebar2.CookieBar
import java.util.*




class RegisterFragment : BaseFragment(), RegisterPresenter.View, ApiEventsListeners.RegisterListener {

    companion object {
        var registerRequest: RegisterRequest =
            RegisterRequest()
    }

    private var age: Int = 0
    private val presenter: RegisterPresenter by lazy { RegisterPresenter(this) }
    private var gender: User.Gender = User.Gender.FEMALE

    override fun onCreateViewId(): Int {
        return R.layout.fragment_register
    }




    override fun viewCreated(view: View?) {

        presenter.init()
        etBirthday.setOnClickListener {
            showDatePickerDialog()
        }
        rgGender.setOnCheckedChangeListener { _, _ ->
            gender = when {
                rbFemale.isChecked -> User.Gender.FEMALE
                rbMale.isChecked -> User.Gender.MALE
                else -> User.Gender.OTHER
            }

        }



        etName.setOnLongClickListener {
            autoFill()
            true
        }
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
                            presenter.isValidBirthday(p0.toString(), age)
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
        val firebaseApiManager: FirebaseApiManager by lazy {
            FirebaseApiManager(
                registerListener = this,
                activity = activity!!
            )
        }
        return firebaseApiManager.createUser(data)
    }


    private fun retrieveRegisterData(): RegisterRequest {
        val register = RegisterRequest()
        register.email = etEmail.text.toString()
        register.password = etPassword.text.toString()
        register.confirmPassword = etPasswordRepeat?.text.toString()
        register.name = etName.text.toString()
        register.birthday = etBirthday.text.toString()
        register.age = age
        register.gender = gender


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
            enableScreenOnRegister(false)
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
            age = getAge(it.get(Calendar.YEAR), it.get(Calendar.MONTH), it.get(Calendar.DAY_OF_MONTH))
            etBirthday.setText(DateUtils.formatTarget(DATE_FORMAT_USER).formatDate(it))
        }
        this.activity?.supportFragmentManager?.let { newFragment.show(it, "") }

    }

    private fun getAge(year: Int, month: Int, day: Int): Int {
        var age = Calendar.getInstance().get(Calendar.YEAR) - year
        if ((Calendar.getInstance().get(Calendar.MONTH) < month) ||
            (Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < day
                    )
        ) age--
        return age
    }


    private fun enableScreenOnRegister(enable: Boolean) {
        tiName.isEnabled = enable
        tiBirthday.isEnabled = enable
        tiPasswordRepeat.isEnabled = enable
        tiPassword.isEnabled = enable
        tiEmail.isEnabled = enable
        btnNext.isEnabled = enable
        btnBack.isEnabled = enable
        rbFemale.isEnabled = enable
        rbMale.isEnabled = enable
        rbOther.isEnabled = enable
        rgGender.isEnabled = enable
    }

    private fun goToNextScreen() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.goToDownloadFragment)
        }
    }

    private fun backToLogin() {
        view?.let {
            Navigation.findNavController(it).popBackStack()
        }
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
            tiEmail.error = getString(R.string.email_error)
        }

        tiEmail.showError(error)
    }

    override fun showPasswordError(error: Boolean) {
        if (error) {
            tiPassword.error = getString(R.string.password_error)
        }
        tiPassword.showError(error)
    }

    override fun enableRegister(enable: Boolean) {
        btnNext.isEnabled = enable
    }


    override fun showPasswordConfirmError(error: Boolean) {
        if (error) {
            tiPasswordRepeat.error = getString(R.string.password_error)
        }
        tiPasswordRepeat.showError(error)
    }

    override fun showNameError(error: Boolean) {
        if (error) {
            tiName.error = getString(R.string.name_error)
        }

        tiName.showError(error)
    }


    override fun showBirthdayError(error: Boolean) {
        if (error) tiBirthday.error = getString(R.string.age_error)
        tiBirthday.showError(error)
    }

    override fun isRegistered(success: Boolean) {
        showProgress(show = false, hasShade = false)
        if (success) {
            goToNextScreen()
        } else {
            Toasty.error(context!!,getString(R.string.register_error_message), 2000, true).show()
            enableScreenOnRegister(true)

        }

    }

    override fun hasToolbar(): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        CookieBar.dismiss(activity)
        showProgress(false,false)

    }


}
