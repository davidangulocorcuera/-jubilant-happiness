package com.example.justfivemins.modules.showUsersScreen.userDetail

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.justfivemins.R
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseDialogFragment
import kotlinx.android.synthetic.main.item_user_detail.*

class UserDetailDialog: BaseDialogFragment(),UsersDetailPresenter.View {



    private val presenter: UsersDetailPresenter by lazy { UsersDetailPresenter(this) }

    override fun onCreateViewId(): Int {
       return R.layout.item_user_detail
    }

    override fun viewCreated(view: View?) {
        presenter.init(arguments)
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setStyle(STYLE_NO_FRAME, android.R.style.Theme)
        }

    }
    @SuppressLint("SetTextI18n")
    override fun loadUser(user: User) {
        tvName.text = user.name
        user.currentLocation?.apply {
            tvLocation.text = "$country,$city"
        }
        tvDescription.text = user.description
        if (user.profileImageUrl.isNotEmpty()) {
            Glide
                .with(activity!!)
                .load(user.profileImageUrl)
                .centerCrop()
                .into(ivProfileImage)

        } else {
            ivProfileImage.setImageResource(R.drawable.no_profile_image)
        }
        tvJob.text = user.jobName
        tvUniversity.text = user.universityName
    }

}