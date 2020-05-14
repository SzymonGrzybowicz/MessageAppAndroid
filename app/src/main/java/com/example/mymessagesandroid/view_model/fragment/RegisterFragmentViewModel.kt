package com.example.mymessagesandroid.view_model.fragment

import androidx.lifecycle.ViewModel
import com.example.mymessagesandroid.model.controler_interface.ILoginController
import com.example.mymessagesandroid.model.dto.User
import com.example.mymessagesandroid.model.sg_controllers.LoginController

class RegisterFragmentViewModel(
	private val mLoginController: ILoginController = LoginController()
) : ViewModel() {

	fun registerUser(user: User){
		mLoginController.registerUser(user)
	}

	val registrationStatus = mLoginController.registrationStatus
}