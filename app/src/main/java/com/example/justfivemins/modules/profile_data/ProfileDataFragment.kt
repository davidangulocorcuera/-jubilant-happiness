package com.example.justfivemins.modules.profile_data

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.example.justfivemins.R
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.utils.DatePickerFragment
import com.example.justfivemins.utils.DateUtils
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*

class ProfileDataFragment : BaseFragment() {
    private var user: User = User()
    private var age: Int = 0


    override fun onCreateViewId(): Int {
        return R.layout.fragment_profile_data
    }

    override fun viewCreated(view: View?) {
        setPersonalData(user)
        setToolbarTitle(getString(R.string.my_data_title))

    }




    private fun setPersonalData(user: User) {

    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment()
        newFragment.arguments = Bundle()
        newFragment.onDateSelected {
            age = getAge(it.get(Calendar.YEAR), it.get(Calendar.MONTH), it.get(Calendar.DAY_OF_MONTH))
            etBirthday.setText(DateUtils.formatTarget(DateUtils.DATE_FORMAT_USER).formatDate(it))
        }
        newFragment.show(this.activity?.supportFragmentManager, "")

    }

    private fun getAge(year: Int, month: Int, day: Int): Int {
        var age = Calendar.getInstance().get(Calendar.YEAR) - year
        if ((Calendar.getInstance().get(Calendar.MONTH) < month) ||
            (Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < day
                    )
        ) age--
        return age
    }

}