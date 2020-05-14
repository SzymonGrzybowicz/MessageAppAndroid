package com.example.mymessagesandroid.view.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.mymessagesandroid.R
import com.example.mymessagesandroid.base.BaseFragment
import com.example.mymessagesandroid.model.application.MessagesApplication
import com.example.mymessagesandroid.model.dto.Message
import com.example.mymessagesandroid.view.util.UiUtils
import com.example.mymessagesandroid.view_model.fragment.ChatFragmentViewModel
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_chat.chat_message_txt
import kotlinx.android.synthetic.main.fragment_chat.message_list
import kotlinx.android.synthetic.main.fragment_chat.message_text
import kotlinx.android.synthetic.main.fragment_chat.sent_btn

class ChatFragment : BaseFragment() {

	override fun getTitle(): String {
		return args.chatWith
	}

	override val withToolbar: Boolean
		get() = true

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		return inflater.inflate(R.layout.fragment_chat, null)
	}

	override fun onStart() {
		super.onStart()
		chat_message_txt.setText(R.string.wait)

		mViewModel.fetchMessages(args.conversationId)
		mViewModel.messagesData.observe(this, Observer {
			mViewModel.markAsRead(args.conversationId)
			onNewMessages(it)
			if (it.isEmpty()) {
				return@Observer
			}
		})
		sent_btn.setOnClickListener {
			val text = message_text.text.toString()
			if (text.isNotEmpty()) {
				val newMessages = mutableListOf<Message>()
				newMessages.addAll(mMessages)
				newMessages.add(
					Message(
						null,
						text,
						System.currentTimeMillis(),
						MessagesApplication.settings.currentUser?.id ?: -1,
						args.conversationId
					)
				)
				onNewMessages(newMessages)
				mViewModel.sentMessage(text, args.conversationId)
				message_text.setText("")
			}
		}

		mPushDisposable = MessagesApplication.pushObservable.subscribe {
			mViewModel.fetchMessages(args.conversationId)
		}
	}

	override fun onStop() {
		super.onStop()
		mPushDisposable?.dispose()
		UiUtils.hideKeyboard(requireActivity())
	}

	private fun onNewMessages(messages: List<Message>) {
		if (messages.isNotEmpty()) {
			chat_message_txt.visibility = View.GONE
			mMessages.clear()
			mMessages.addAll(messages)
			mAdapter =
				ChatAdapter(requireContext(), mMessages, MessagesApplication.settings.currentUser?.id ?: -1)
			message_list.adapter = mAdapter
			message_list.setSelection(mMessages.size - 1)
		} else {
			chat_message_txt.visibility = View.VISIBLE
			chat_message_txt.setText(R.string.no_messages)
		}
	}

	private val args: ChatFragmentArgs by navArgs()
	private val mViewModel by viewModels<ChatFragmentViewModel>()
	private var mPushDisposable: Disposable? = null
	private var mMessages = mutableListOf<Message>()
	private var mAdapter: ChatAdapter? = null
}