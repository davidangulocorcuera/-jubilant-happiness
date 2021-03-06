package com.example.justfivemins.modules.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import com.example.justfivemins.utils.setVisible
import kotlinx.android.synthetic.main.toolbar.view.*

abstract class BaseDialogFragment: DialogFragment(), MainMVP.View  {
    val LOG_TAG = this::class.java.simpleName

    protected abstract fun onCreateViewId(): Int
    protected abstract fun viewCreated(view: View?)

    var configurator: Configurator? = null


    override fun onViewCreated(
            view: View,
            savedInstanceState: Bundle?
    ) {
        viewCreated(view)
    }

    override fun onResume() {
        super.onResume()
        setUpToolbar()
    }

    fun setToolbarTitle(title: String?) {
        baseActivity?.setToolbarText(title)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (configurator == null) {
            configurator = Configurator.instance
            configurator!!.showOnlyToolbarTitle(showOnlyToolbarTitle())
            configurator!!.hasOwnToolbar(false)
        }
    }

    fun showOnlyToolbarTitle(): Boolean {
        return false
    }

    fun setUpToolbar() {
        if (configurator!!.hasToolbar && baseActivity?.currentToolbar == null) {
            baseActivity?.initToolbar()
        }
        val actionBar = baseActivity?.supportActionBar

        if (actionBar != null) {
            setToolbarTitle(configurator!!.title)
            configurator!!.checkDefaults()
            baseActivity?.setToolbartTextColor(configurator!!.titleColor)
            baseActivity?.setToolbarColor(configurator!!.toolbarColor)
            if (!configurator!!.hasToolbar && !configurator!!.hasOwnToolbar) {
                actionBar.hide()
            } else if (!actionBar.isShowing) {
                actionBar.show()
            }

            baseActivity?.currentToolbar?.setVisible(configurator!!.hasToolbar)

        }
    }

    fun hideToolbar() {
        val actionBar = baseActivity?.supportActionBar
        actionBar?.hide()
    }

    fun showToolbar() {
        val actionBar = baseActivity?.supportActionBar
        actionBar?.show()
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



    override fun hideKeyboard() {
        baseActivity?.hideKeyboard()
    }

    fun openKeyboard(view: View) {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInputFromWindow(view.applicationWindowToken,
                InputMethodManager.SHOW_FORCED, 0)
    }







}