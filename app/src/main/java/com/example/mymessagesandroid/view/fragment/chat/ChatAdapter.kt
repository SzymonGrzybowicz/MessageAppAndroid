package com.example.mymessagesandroid.view.fragment.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.mymessagesandroid.R
import com.example.mymessagesandroid.model.dto.Message
import kotlinx.android.synthetic.main.chat_list_item.view.message_incoming_layout
import kotlinx.android.synthetic.main.chat_list_item.view.message_incoming_text
import kotlinx.android.synthetic.main.chat_list_item.view.message_outgoing_layout
import kotlinx.android.synthetic.main.chat_list_item.view.message_outgoing_text

class ChatAdapter(private val mContext: Context, private val mMessages: List<Message>, private val currentUserId: Long) :
	ArrayAdapter<Message>(
		mContext,
		R.layout.chat_list_item,
		mMessages
	) {

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val listItem =
			convertView ?: LayoutInflater.from(mContext).inflate(R.layout.chat_list_item, parent, false)
		val message = mMessages[position]
		when (message.creator_id) {
			currentUserId -> setOutgoingMessage(message, listItem)
			else -> setIncomingMessage(message, listItem)
		}
		return listItem
	}

	override fun getViewTypeCount(): Int {
		return mMessages.size
	}

	override fun getItemViewType(position: Int): Int {
		return position
	}

	override fun getCount(): Int {
		return mMessages.size
	}

	override fun getItem(position: Int): Message {
		return mMessages[position]
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	private fun setIncomingMessage(message: Message, view: View) {
		view.message_outgoing_layout.visibility = View.GONE
		view.message_incoming_text.text = message.content
	}

	private fun setOutgoingMessage(message: Message, view: View) {
		view.message_incoming_layout.visibility = View.GONE
		view.message_outgoing_text.text = message.content
	}
}