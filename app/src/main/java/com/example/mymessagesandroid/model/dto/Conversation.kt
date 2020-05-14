package com.example.mymessagesandroid.model.dto

data class Conversation(
	val conversation_id: Long?,
	val with: List<User>,
	val unread: Boolean)