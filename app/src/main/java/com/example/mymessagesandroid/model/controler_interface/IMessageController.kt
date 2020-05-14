package com.example.mymessagesandroid.model.controler_interface

import androidx.lifecycle.LiveData
import com.example.mymessagesandroid.model.dto.Message

interface IMessageController {

	fun sentMessage(message: Message)
	fun fetchMessages(conversationId: Long)
	val messagesData: LiveData<List<Message>>
}