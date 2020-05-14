package com.example.mymessagesandroid.view.fragment.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mymessagesandroid.BuildConfig
import com.example.mymessagesandroid.R
import com.example.mymessagesandroid.base.BaseFragment
import com.example.mymessagesandroid.model.application.MessagesApplication
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status.ACCEPTED
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status.IN_PROGRESS
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status.REJECTED
import com.example.mymessagesandroid.view.util.UiUtils
import com.example.mymessagesandroid.view_model.fragment.LoginFragmentViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_login.login_btn
import kotlinx.android.synthetic.main.fragment_login.login_mail_edit_text
import kotlinx.android.synthetic.main.fragment_login.login_message_txt
import kotlinx.android.synthetic.main.fragment_login.login_password_edit_text
import kotlinx.android.synthetic.main.fragment_login.register_btn

class LoginFragment : BaseFragment() {

	override fun getTitle(): String {
		return resources.getString(R.string.login)
	}

	override val withToolbar: Boolean
		get() = true

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		return inflater.inflate(R.layout.fragment_login, null)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mViewModel.loginStatus.observe(this, Observer { status ->
			onStatus(status)
		})
	}

	override fun onStart() {
		super.onStart()
		login_btn.setOnClickListener {
			val mail = login_mail_edit_text.text.toString()
			val password = login_password_edit_text.text.toString()
			if (mail.isNotEmpty() && password.isNotEmpty()) {
				if (!UiUtils.isValidEmail(mail)) {
					login_message_txt.setText(R.string.mail_invalid)
				}
				mViewModel.onLogin(mail, password)
			}
		}

		register_btn.setOnClickListener {
			if (it.findNavController().currentDestination?.id == R.id.loginFragment) {
				findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
			}
		}

		if (BuildConfig.DEBUG) {
			login_mail_edit_text.setText("1@1.1")
			login_password_edit_text.setText("1")
		}
	}

	private fun onStatus(status: Status) {
		when (status) {
			REJECTED -> {
				login_message_txt.setText(R.string.try_again)
				login_btn.isEnabled = true
			}
			IN_PROGRESS -> {
				login_message_txt.setText(R.string.wait)
				login_btn.isEnabled = false
			}
			ACCEPTED -> {
				setFirebaseService(MessagesApplication.settings.currentUser?.id ?: -1)
				if (findNavController().currentDestination?.id == R.id.loginFragment) {
					findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToConversationsFragment())
				}

			}
		}
	}

	//todo move it to better place
	private fun setFirebaseService(userId: Long){
		FirebaseInstanceId.getInstance().instanceId
			.addOnCompleteListener(OnCompleteListener { task ->
				if (!task.isSuccessful) {
					Log.w("LoginFragment", "getInstanceId failed", task.exception)
					return@OnCompleteListener
				}

				val token = task.result?.token

				val msg = "token($token)"
				token?.let {
					mViewModel.setAuthToken(userId, token)
				}
				Log.d("LoginFragment", msg)
			})
	}

	private val mViewModel by viewModels<LoginFragmentViewModel>()
}
