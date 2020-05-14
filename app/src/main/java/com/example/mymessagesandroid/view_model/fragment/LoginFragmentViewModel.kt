package com.example.mymessagesandroid.view_model.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mymessagesandroid.model.controler_interface.ILoginController
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status
import com.example.mymessagesandroid.model.sg_controllers.LoginController
import com.example.mymessagesandroid.model.dto.User

class LoginFragmentViewModel(
private val mLoginController: ILoginController = LoginController()
) : ViewModel() {

	fun onLogin(mail: String, password: String) {
		val user = User(mail = mail, password = password)
		mLoginController.login(user)
	}

	fun setAuthToken(userId: Long, authToken: String) {
		mLoginController.sentFirebaseAuthToken(userId, authToken)
	}

	val loginStatus: LiveData<Status> = mLoginController.loginStatus
}