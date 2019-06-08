package com.example.justfivemins.modules.messages.chat

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import com.example.justfivemins.R
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_text_message.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor
import org.jetbrains.anko.wrapContent
import java.text.SimpleDateFormat

class TextMessageItem(val message: TextMessage, val context: Context) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.containerView.tvMessage.text = message.text
        setTimeText(viewHolder)
        setMessageRootGravity(viewHolder)

    }

    private fun setTimeText(viewHolder: ViewHolder) {
        val dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        viewHolder.containerView.tvMessageDate.text = dateFormat.format(message.time)

    }

    private fun setMessageRootGravity(viewHolder: ViewHolder) {
        if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            viewHolder.containerView.messageRoot.apply {
                backgroundResource = R.drawable.rect_oval_white
                viewHolder.containerView.tvMessageDate.textColor = R.color.black
                viewHolder.containerView.tvMessage.textColor = R.color.black
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                this.layoutParams = lParams
            }
        } else {
            viewHolder.containerView.messageRoot.apply {
                backgroundResource = R.drawable.rect_oval_primary
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = lParams
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_text_message
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is TextMessageItem){
            return false
        }
        if(this.message != other.message){
            return false
        }
        return true
    }

    override fun equals(other: Any?): Boolean {
       return isSameAs(other as? TextMessageItem)
    }
}