package com.example.justfivemins.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.justfivemins.MainActivity
import com.example.justfivemins.R
import com.example.justfivemins.modules.home.HomeActivity
import com.example.justfivemins.modules.base.BaseActivity
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.base.Configurator
import com.example.justfivemins.modules.content.ContentFragment
import com.example.justfivemins.modules.friends.FriendsFragment
import com.example.justfivemins.modules.gallery.GalleryFragment
import com.example.justfivemins.modules.login.LoginFragment
import com.example.justfivemins.modules.map.MapFragment
import com.example.justfivemins.modules.messages.MessagesFragment
import com.example.justfivemins.modules.profile_data.ProfileDataFragment
import com.example.justfivemins.modules.register.RegisterFragment
import java.io.Serializable


class Navigator {

    private val LOG_TAG = Navigator::class.java.simpleName
    private var intentToLaunch: Intent? = null
    private lateinit var activity: BaseActivity
    private var extras: Bundle? = null
    private var finishCurrent: Boolean = false
    private var addBackStack: Boolean = false
    private var animated = true
    private var preserve: Boolean = false
    private var hasTransition = false
    private var configurator: Configurator? = null
    private var newTaskFlag = false
    private var targetFragment: BaseFragment? = null
    private var targetCode: Int = 0
    private var containerId = R.id.fragment_container

    constructor(baseActivity: BaseActivity) {
        intentToLaunch = Intent()
        this.activity = baseActivity
    }

    constructor() {
        intentToLaunch = Intent()
    }

    fun getExtras(): Bundle? {
        Log.d(LOG_TAG, extras.toString())
        return extras
    }

    fun setExtras(extras: Bundle): Navigator {
        this.extras = extras
        addExtrasToIntent()
        return this
    }

    fun configurator(configurator: Configurator): Navigator {
        this.configurator = configurator
        return this
    }

    fun clearExtras(): Navigator {
        if (extras != null) extras!!.clear()
        if (intentToLaunch != null) {
            intentToLaunch!!.replaceExtras(Bundle())
        }
        return this
    }

    private fun navigate(context: Context?) {

        if (!animated && context != null && context is Activity) {
            context.overridePendingTransition(0, 0)
        }

        if (context != null) {
            if (newTaskFlag) intentToLaunch!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (!animated) intentToLaunch!!.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            context.startActivity(intentToLaunch)
        }

        if (finishCurrent && context != null && context is Activity) {

            context.finish()
        }
        extras = null
    }

    fun navigate(
        context: Context?,
        cls: Class<*>
    ) {
        if (context != null) {
            intentToLaunch!!.setClass(context, cls)
            navigate(context)
        }
    }

    private fun navigate(
        context: Context?,
        cleanStack: Boolean = false
    ) {

        if (!animated && context != null && context is Activity) {
            context.overridePendingTransition(0, 0)
        }

        if (context != null) {
            if (cleanStack) intentToLaunch!!.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
            if (newTaskFlag) intentToLaunch!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (!animated) intentToLaunch!!.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            context.startActivity(intentToLaunch)
        }

        if (finishCurrent && context != null && context is Activity) {

            context.finish()
        }
        extras = null
    }

    private fun addExtrasToIntent() {
        if (extras != null && !extras!!.isEmpty) {
            intentToLaunch!!.putExtras(extras!!)
        }
    }

    fun finishCurrent(finishCurrent: Boolean): Navigator {
        this.finishCurrent = finishCurrent
        return this
    }

    fun addExtra(
        key: String,
        value: String
    ): Navigator {
        if (extras == null) {
            extras = Bundle()
        }
        extras!!.putString(key, value)
        addExtrasToIntent()
        return this
    }

    fun addExtra(
        key: String,
        value: Boolean
    ): Navigator {
        if (extras == null) {
            extras = Bundle()
        }
        extras!!.putBoolean(key, value)
        addExtrasToIntent()
        return this
    }

    fun addExtra(
        key: String,
        value: Serializable
    ): Navigator {
        if (extras == null) {
            extras = Bundle()
        }
        extras!!.putSerializable(key, value)
        addExtrasToIntent()
        return this
    }

    fun deleteExtra(
        key: String
    ): Navigator {
        if (extras != null) {

            extras!!.remove(key)
            addExtrasToIntent()
        }
        return this
    }

    fun newTaskFlag(newTaskFlag: Boolean): Navigator {
        this.newTaskFlag = newTaskFlag
        return this
    }

    fun addExtra(
        key: String,
        value: Int
    ): Navigator {
        if (extras == null) {
            extras = Bundle()
        }
        extras!!.putInt(key, value)
        addExtrasToIntent()
        return this
    }

    fun addBackStack(addBackStack: Boolean): Navigator {
        this.addBackStack = addBackStack
        return this
    }

    /**
     * Only works for Activities
     */
    fun animated(animated: Boolean): Navigator {
        this.animated = animated
        return this
    }

    fun preserve(preserve: Boolean): Navigator {
        this.preserve = preserve
        return this
    }

    fun containerId(containerId: Int): Navigator {
        this.containerId = containerId
        return this
    }

    fun navigate(
        activity: AppCompatActivity?,
        fragmentClass: Class<out BaseFragment>,
        params: Bundle?
    ) {
        if (activity != null) navigate(activity, fragmentClass, addBackStack)
    }

    fun navigate(
        activity: AppCompatActivity,
        clazz: Class<out BaseFragment>
    ) {
        navigate(activity, clazz, null)
    }

    fun goBack() {
        activity.supportFragmentManager.popBackStack()
        /*val reloadTravels = extras?.getBoolean("reloadTravels", false)
        if (reloadTravels != null && reloadTravels) {
            activity.supportFragmentManager.findFragmentByTag(TravelFragment::LOG_TAG.toString()).onResume()
        }*/
    }

    fun navigate(clazz: Class<out BaseFragment>) {
        navigate(activity, clazz, extras)
    }

    fun hasTransition(hasTransition: Boolean): Navigator {
        this.hasTransition = hasTransition
        return this
    }

    fun navigateToHome() {
        navigate(activity, HomeActivity::class.java)
    }
    fun navigateToContentFragment(){
        navigate(activity, ContentFragment::class.java)
    }

    fun navigateToMain() {
        navigate(activity, MainActivity::class.java)
    }

    fun navigateToProfileData() {
        navigate(activity, ProfileDataFragment::class.java)
    }
    fun navigateToMessages() {
        navigate(activity, MessagesFragment::class.java)
    }
    fun navigateToGallery() {
        navigate(activity, GalleryFragment::class.java)
    }
    fun navigateToFriends() {
        navigate(activity, FriendsFragment::class.java)
    }

    fun navigateToMap() {
        navigate(activity, MapFragment::class.java)
    }

    fun navigateToLogin() {
        navigate(activity, LoginFragment::class.java)
    }
    fun navigateToRegister() {
        navigate(activity, RegisterFragment::class.java)
    }


    fun navigate(
        context: Context?,
        cls: Class<*>,
        cleanStack: Boolean = false
    ) {
        if (context != null) {
            intentToLaunch!!.setClass(context, cls)
            if (cleanStack)
                navigate(context, cleanStack)
            else
                navigate(context)
        }
    }

    fun navigate(
        activity: AppCompatActivity,
        fragmentClass: Class<out BaseFragment>,
        addBackStack: Boolean
    ) {
        val fragmentManager = activity.supportFragmentManager
        val trans = fragmentManager.beginTransaction()
        try {
            var fragment: BaseFragment = fragmentClass.newInstance()
            val tag = fragment.LOG_TAG
            val oldFragment = fragmentManager.findFragmentByTag(tag)

            if (oldFragment != null && !preserve) {
                fragment = oldFragment as BaseFragment
            } else {
                fragment.configurator = configurator
                fragment.arguments = extras
            }
            if (targetFragment != null) {
                fragment.setTargetFragment(targetFragment, targetCode)
            }

            if (hasTransition) {
                //trans.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
            }

            trans.replace(containerId, fragment, tag)

            //activity.replaceFragment(fragment, R.id.fragment_container, tag)

            if (addBackStack) {
                trans.addToBackStack(null)
            }

            trans.commitAllowingStateLoss()
            this.preserve = false
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun navigate(
        f: Fragment,
        cleanStack: Boolean = false,
        addBackStack: Boolean = false
    ) {
        val ft = activity.supportFragmentManager.beginTransaction()
        if (cleanStack) {
            clearBackStack()
        }
        setUpTransition(ft)
        ft.replace(R.id.fragment_container, f)
        if (addBackStack) {
            ft.addToBackStack(null)
        }
        ft.commit()
    }

    private fun findCurrentFragment(): Fragment? {
        val fragmentManager = activity.supportFragmentManager
        var currentFragment: Fragment? = fragmentManager.fragments[fragmentManager.fragments.size - 1]
        if (currentFragment == null) {
            for (fragment in fragmentManager.fragments) {
                if (fragment != null && fragment.isVisible) {
                    currentFragment = fragment
                    break
                }
            }
        }
        return currentFragment
    }

    /*  fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, tag: String) {
          supportFragmentManager.inTransaction { add(frameId, fragment, tag) }
      }*/

    fun AppCompatActivity.replaceFragment(
        fragment: Fragment,
        frameId: Int,
        tag: String
    ) {
        supportFragmentManager.inTransaction { replace(frameId, fragment, tag) }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func()
            .commit()
    }

    fun setUpTransition(transaction: FragmentTransaction) {
        if (hasTransition) {

        }
    }

    fun clearBackStack(): Navigator {
        val manager = activity.supportFragmentManager
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0)
            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        return this
    }

    fun target(fragment: BaseFragment, code: Int): Navigator {
        this.targetFragment = fragment
        this.targetCode = code
        return this

    }

    fun clearTarget(): Navigator {
        this.targetCode = 0
        this.targetFragment = null
        return this
    }


}