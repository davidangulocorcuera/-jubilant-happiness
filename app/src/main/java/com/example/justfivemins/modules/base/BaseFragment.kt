package com.example.justfivemins.modules.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.justfivemins.utils.Navigator



abstract class BaseFragment : Fragment(), MainMVP.View {
    val LOG_TAG = this::class.java.simpleName



    protected abstract fun onCreateViewId(): Int
    protected abstract fun viewCreated(view: View?)
    val navigator: Navigator
        get() {
            return baseActivity?.navigator!!
        }

    var configurator: Configurator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (configurator == null) {
            configurator = Configurator.instance
            configurator!!.hasToolbar(hasToolbar())
            configurator!!.hasToolbarBackButton(hasToolbarBackButton())
            configurator!!.showOnlyToolbarTitle(showOnlyToolbarTitle())
            configurator!!.hasOwnToolbar(false)
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        viewCreated(view)
    }

    fun showProgress(show: Boolean, hasShade: Boolean) {
        baseActivity?.showProgress(show, hasShade)
    }

    fun setToolbarTitle(title: String?) {
        baseActivity?.setToolbarText(title)
    }

    open fun hasToolbar(): Boolean {
        return false
    }


    fun showOnlyToolbarTitle(): Boolean {
        return false
    }


    fun hideToolbar() {
        val actionBar = baseActivity?.supportActionBar
        actionBar?.hide()
    }

    fun showToolbar() {
        val actionBar = baseActivity?.supportActionBar
        actionBar?.show()
    }

    fun showBottomMenu() {
        //activity?.bottomNavigation?.setVisible(true)
    }

    fun hideBottomMenu() {
        //activity?.bottomNavigation?.setVisible(false)
    }

    open fun hasToolbarBackButton(): Boolean {
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(onCreateViewId(), container, false)
    }

    val baseActivity: BaseActivity?
        get() {
            return activity as? BaseActivity
        }

    open fun willConsumeBackPressed(): Boolean {
        return false
    }


    override fun hideKeyboard() {
        baseActivity?.hideKeyboard()
    }


    fun openKeyboard(view: View) {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(
            view.applicationWindowToken,
            InputMethodManager.SHOW_FORCED, 0
        )
    }

    override fun goBack() {
        baseActivity?.goBack()

    }
    fun enableDrawerMenu(enable: Boolean) {
        baseActivity?.enableDrawerMenu(enable)
    }




}