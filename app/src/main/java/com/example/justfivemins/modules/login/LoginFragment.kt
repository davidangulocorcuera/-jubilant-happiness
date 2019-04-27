package com.example.justfivemins.modules.login

import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.modules.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment(),LoginFragmentPresenter.View {
    private val presenter: LoginFragmentPresenter by lazy { LoginFragmentPresenter(this) }

    override fun onCreateViewId(): Int {
        return R.layout.fragment_login
    }

    override fun viewCreated(view: View?) {
        presenter.init(arguments)
        setButtonsListeners()
    }
    private fun setButtonsListeners(){
        btnLogin.setOnClickListener {
            goToHome()
        }
        btnRegister.setOnClickListener {

        }
    }

    override fun goToHome() {
        navigator.let {
            it.finishCurrent(true)
            it.navigateToHome()
        }

    }
}
