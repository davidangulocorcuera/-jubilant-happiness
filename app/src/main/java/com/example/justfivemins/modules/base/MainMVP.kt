package com.example.justfivemins.modules.base


class MainMVP {

    interface View {
        fun showProgress(show: Boolean, hasShade: Boolean = true)
        fun showMessage(message: String)
        fun showError(error: String)
        fun goBack()
        fun hideKeyboard()

    }

    interface Presenter {

    }

    interface Interactor {
        fun execute()

        interface CallbackResult<in T> {
            fun onSuccess(result: T)
            fun onFinish() {}
        }
    }

}