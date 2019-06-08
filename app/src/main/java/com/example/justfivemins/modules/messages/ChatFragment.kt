package com.example.justfivemins.modules.messages


import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.justfivemins.R
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.example.justfivemins.modules.messages.chat.TextMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*


class ChatFragment : BaseFragment(), ChatPresenter.View {
    private val presenter: ChatPresenter by lazy { ChatPresenter(this) }
    private val args: ChatFragmentArgs by navArgs()
    private lateinit var otherUser: User
    private val firebaseApiManager: FirebaseApiManager by lazy {
        FirebaseApiManager(
            activity = activity!!
        )
    }
    private lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section


    override fun onCreateViewId(): Int {
        return R.layout.fragment_chat
    }

    override fun viewCreated(view: View?) {
        presenter.init(args)
        val otherUserId = otherUser.id
        firebaseApiManager.getOrCreateChatChannel(otherUserId) { channelId ->
            messagesListenerRegistration =
                firebaseApiManager.addChatMessagesListener(channelId, context!!, this::updateRecyclerView)

            btnSend.setOnClickListener {
                val messageToSend = TextMessage(
                    etChatText.text.toString(),
                    Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser?.uid!!
                )
                etChatText.setText("")
                firebaseApiManager.sendMessage(messageToSend, channelId)
            }
        }

    }

    private fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            rvChat.apply {
                layoutManager = LinearLayoutManager(this@ChatFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(messagesSection)
                }

            }
            shouldInitRecyclerView = true
        }

        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView) {
            init()
        } else {
            updateItems()
        }
        rvChat.scrollToPosition(rvChat.adapter?.itemCount!! - 1)
    }


    override fun setUser(user: User) {
        otherUser = user
    }


}
