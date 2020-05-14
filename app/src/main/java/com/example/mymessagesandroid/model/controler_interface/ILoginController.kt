package com.example.mymessagesandroid.model.controler_interface

import androidx.lifecycle.LiveData
import com.example.mymessagesandroid.model.dto.User

interface ILoginController {

	fun login(user: User)
	fun sentFirebaseAuthToken(userId: Long, authToken: String)
	fun registerUser(user: User)

	val registrationStatus: LiveData<Status>
	val loginStatus: LiveData<Status>

	enum class Status{
		ACCEPTED,
		IN_PROGRESS,
		REJECTED
	}
}