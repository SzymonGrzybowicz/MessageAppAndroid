package com.example.mymessagesandroid.view_model.fragment

import androidx.lifecycle.ViewModel
import com.example.mymessagesandroid.model.application.MessagesApplication
import com.example.mymessagesandroid.model.controler_interface.IConversationController
import com.example.mymessagesandroid.model.controler_interface.IMessageController
import com.example.mymessagesandroid.model.dto.Message
import com.example.mymessagesandroid.model.sg_controllers.ConversationController
import com.example.mymessagesandroid.model.sg_controllers.MessageController

class ChatFragmentViewModel(
	private val mConversationController: IConversationController = ConversationController(),
	private val mMessageController: IMessageController = MessageController()
) : ViewModel() {

	fun sentMessage(message: String, conversationId: Long) {
		mMessageController.sentMessage(
			Message(
				null,
				message,
				System.currentTimeMillis(),
				MessagesApplication.settings.currentUser?.id ?: -1,
				conversationId
			)
		)
	}

	fun fetchMessages(conversationId: Long) {
		mMessageController.fetchMessages(conversationId)
	}

	fun markAsRead(conversationId: Long) {
		mConversationController.markAsRead(conversationId);
	}

	val messagesData = mMessageController.messagesData
}