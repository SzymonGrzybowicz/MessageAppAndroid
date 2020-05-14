package com.example.mymessagesandroid.model.dto

data class Message(
	val id: Long?,
	val content: String,
	val timestamp: Long,
	val creator_id: Long,
	val conversation_id: Long
)