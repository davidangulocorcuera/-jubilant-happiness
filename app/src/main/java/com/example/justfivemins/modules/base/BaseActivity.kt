package com.example.justfivemins.modules.base

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.justfivemins.R
import com.example.justfivemins.modules.home.home_drawer.DrawerLocker
import com.example.justfivemins.utils.Navigator
import com.example.justfivemins.utils.color
import com.example.justfivemins.utils.setVisible
import kotlinx.android.synthetic.main.activity_home.*


abstract class BaseActivity : AppCompatActivity(), MainMVP.View {

    var LOG_TAG = BaseActivity::class.java.simpleName
    fun getTag(): String {
        return this.javaClass.simpleName
    }

    val navigator: Navigator by lazy { Navigator(this) }

    var toolbar_title: TextView? = null
    var currentToolbar: Toolbar? = null

    protected abstract fun onCreateViewId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LOG_TAG = getTag()

        val layoutId = onCreateViewId()
        if (layoutId != 0) {
            setContentView(layoutId)
        }

        initToolbar(findViewById(R.id.toolbar))
    }

    fun initToolbar(toolbar: Toolbar? = null) {
        var current = toolbar
        if (current == null) {
            current = activity_home?.findViewById(R.id.toolbar)
        }
        if (current != null) {
            setSupportActionBar(current)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
            current.setNavigationOnClickListener { onBackPressed() }

            current.title = ""
            if (titleColor != 0)
                current.setTitleTextColor(getResourceColor(titleColor))

            this.currentToolbar = current
            toolbar_title = currentToolbar?.findViewById(R.id.toolbar_title)
            supportActionBar?.show()
        }
    }

    fun callOnResumeByTag(tag: String) {
        var foundFragment: BaseFragment? = null
        supportFragmentManager.fragments.forEach {
            if ((it as? BaseFragment)?.LOG_TAG == tag) {
                foundFragment = it
                foundFragment?.onResume()
            }
        }

    }

    fun setBackButtonColor(color: Int) {
        currentToolbar?.navigationIcon?.setTint(getResourceColor(color))
    }

    fun setToolbarColor(toolbarColor: Int) {
        val actionBar = supportActionBar
        if (actionBar != null && toolbarColor != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                actionBar.setBackgroundDrawable(ColorDrawable(getResourceColor(toolbarColor)))
            } else {
                actionBar.setBackgroundDrawable(ColorDrawable(getResourceColor(toolbarColor)))
            }
        }
    }

    fun getResourceColor(id: Int): Int {
        return applicationContext.color(id)
    }

    fun setToolbartTextColor(titleColor: Int) {
        currentToolbar?.setTitleTextColor(getResourceColor(titleColor))
        toolbar_title?.setTextColor(getResourceColor(titleColor))
    }

    fun enableToolbarBackButton(enable: Boolean) {
        val actionBar = supportActionBar
        val params = toolbar_title?.layoutParams as? RelativeLayout.LayoutParams
        if (enable) {
            params?.marginEnd = getActionBarHeight()
            actionBar?.setDisplayHomeAsUpEnabled(true)
            actionBar?.setDisplayShowHomeEnabled(true)
        } else {
            params?.marginEnd = 0
            params?.marginStart = 0
            actionBar?.setDisplayHomeAsUpEnabled(false)
            actionBar?.setDisplayShowHomeEnabled(false)
        }
    }

    fun getActionBarHeight(): Int {
        val tv = TypedValue()
        var actionBarHeight = 0
        if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics);
        }
        return actionBarHeight
    }

    fun willConsumeBackPressed(): Boolean {
        var willConsumeBackPressed = false
        val fragments = supportFragmentManager.fragments
        for (i in fragments.indices) {
            val fragment = fragments[i]
            if (fragment is BaseFragment) {
                if (fragment.willConsumeBackPressed()) {
                    willConsumeBackPressed = true
                    break
                }
            }
        }

        return willConsumeBackPressed
    }

    override fun onBackPressed() {
        if (!willConsumeBackPressed()) {
            super.onBackPressed()
        }
    }



    override fun hideKeyboard() {
        val view = this.currentFocus
        hideKeyboard(view)
    }

    fun hideKeyboard(view: View?) {
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun setToolbarText(title: String?) {
        if (toolbar_title != null && title != null) {
            toolbar_title?.visibility = View.VISIBLE
            toolbar_title?.text = title
        }
    }
    fun showProgress(show: Boolean, hasShade: Boolean) {
        val progress = findViewById<ProgressBar>(R.id.progress)
        val progressContainer = findViewById<View>(R.id.progressContainer)
        progressContainer?.setVisible(show && hasShade)
        progress?.setVisible(show)
    }

    fun centerToolbarText(center: Boolean) {
        if (center) {
            toolbar_title?.layoutParams?.width = LinearLayout.LayoutParams.MATCH_PARENT
        } else {
            toolbar_title?.layoutParams?.width = LinearLayout.LayoutParams.WRAP_CONTENT
        }
    }
    override fun goBack() {
        navigator.goBack()
    }
    fun enableDrawerMenu(enable: Boolean) {
        (this as? DrawerLocker)?.setDrawerEnabled(enable)
        addMarginToToolbarTitle(enable)
    }
    private fun addMarginToToolbarTitle(enable: Boolean) {
        val params = toolbar_title?.layoutParams as? RelativeLayout.LayoutParams
        if (enable) {
            params?.marginEnd = getActionBarHeight()
            if (params != null) {
                toolbar_title?.post {
                    if (toolbar_title != null)
                        toolbar_title?.layoutParams = params
                }
            }
        } else {
            params?.marginEnd = 0
            params?.marginStart = 0
        }
    }


}
