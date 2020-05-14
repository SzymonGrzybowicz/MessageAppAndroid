package com.example.mymessagesandroid.model.controler_interface

import androidx.lifecycle.LiveData
import com.example.mymessagesandroid.model.dto.Conversation

interface IConversationController {
	fun fetchConversations(userId: Long)
	fun createConversation(userId:Long, conversation: Conversation)
	fun markAsRead(conversationId: Long)

	val conversationsData: LiveData<List<Conversation>>
}