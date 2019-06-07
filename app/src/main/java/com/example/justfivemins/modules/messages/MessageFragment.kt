package com.example.justfivemins.modules.messages


import android.os.Message
import android.util.Log
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.justfivemins.R
import com.example.justfivemins.api.firebase.FirebaseApiManager
import com.example.justfivemins.model.User
import com.example.justfivemins.modules.base.BaseFragment
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item


class MessageFragment : BaseFragment(), MessagePresenter.View {
    private val presenter: MessagePresenter by lazy { MessagePresenter(this) }
    private val args: MessageFragmentArgs by navArgs()
    private lateinit var otherUser: User
    private val firebaseApiManager: FirebaseApiManager by lazy {
        FirebaseApiManager(
            activity = activity!!
        )
    }
    private lateinit var messagesListenerRegistration: ListenerRegistration


    override fun onCreateViewId(): Int {
        return R.layout.fragment_messages
    }

    override fun viewCreated(view: View?) {
        presenter.init(args)
        presenter.init(args)
        val otherUserId = otherUser.id
        firebaseApiManager.getOrCreateChatChannel(otherUserId) { channelId ->
            messagesListenerRegistration = firebaseApiManager.addChatMessagesListener(channelId, context!!,this::onMessagesChanged)
        }

    }

    private fun onMessagesChanged(message: List<Item>){
        Log.v("taag","se supone que funciona algo")
    }

    override fun setUser(user: User) {
        otherUser = user
    }


}
