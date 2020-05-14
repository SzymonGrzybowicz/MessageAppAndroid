package com.example.mymessagesandroid.view.fragment.coversations

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.mymessagesandroid.R
import com.example.mymessagesandroid.model.dto.Conversation
import kotlinx.android.synthetic.main.conversation_list_item.view.conversation_mail
import kotlinx.android.synthetic.main.conversation_list_item.view.conversation_name
import kotlinx.android.synthetic.main.conversation_list_item.view.conversation_unread_image

class ConversationsAdapter(private val mContext: Context, private val mConversations: List<Conversation>) :
	ArrayAdapter<Conversation>(
		mContext,
		R.layout.conversation_list_item,
		mConversations
	) {

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val listItem =
			convertView ?: LayoutInflater.from(mContext).inflate(R.layout.conversation_list_item, parent, false)
		val conversation = mConversations[position]
		listItem.conversation_name.text = conversation.with[0].name
		listItem.conversation_mail.text = conversation.with[0].mail
		listItem.conversation_unread_image.visibility = if(conversation.unread) View.VISIBLE else View.GONE
		return listItem
	}

	override fun getViewTypeCount(): Int {
		return mConversations.size
	}

	override fun getItemViewType(position: Int): Int {
		return position
	}

	override fun getCount(): Int {
		return mConversations.size
	}

	override fun getItem(position: Int): Conversation {
		return mConversations[position]
	}

	override fun getItemId(position: Int): Long {
		return mConversations[position].conversation_id ?: -1
	}

	fun getName(position: Int): String {
		return mConversations[position].with[0].name ?: ""
	}
}