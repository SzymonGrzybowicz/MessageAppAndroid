package com.example.mymessagesandroid.view_model.fragment

import androidx.lifecycle.ViewModel
import com.example.mymessagesandroid.model.controler_interface.ILoginController
import com.example.mymessagesandroid.model.dto.User
import com.example.mymessagesandroid.model.sg_controllers.LoginController

class WaitFragmentViewModel (
	private val mLoginController: ILoginController = LoginController()
) : ViewModel() {

	fun loginUser(user: User) {
		mLoginController.login(user)
	}

	val loginStatus = mLoginController.loginStatus
}