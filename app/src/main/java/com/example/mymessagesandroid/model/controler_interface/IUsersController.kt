package com.example.mymessagesandroid.model.controler_interface

import androidx.lifecycle.LiveData
import com.example.mymessagesandroid.model.dto.User

interface IUsersController {

	fun fetchUsers()

	val usersData: LiveData<List<User>>
}