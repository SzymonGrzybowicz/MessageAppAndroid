package com.example.mymessagesandroid.view.fragment.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mymessagesandroid.R
import com.example.mymessagesandroid.R.layout
import com.example.mymessagesandroid.base.BaseFragment
import com.example.mymessagesandroid.model.application.MessagesApplication
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status.ACCEPTED
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status.REJECTED
import com.example.mymessagesandroid.view_model.fragment.WaitFragmentViewModel

class SplashFragment : BaseFragment() {

	override fun getTitle(): String {
		return ""
	}

	override val withToolbar: Boolean
		get() = false

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(layout.fragment_wait, container, false)
	}

	override fun onStart() {
		super.onStart()
		val user = MessagesApplication.settings.currentUser

		if (user == null) {
			navigateToLogin()
		} else {
			mViewModel.loginUser(user)
		}

		mViewModel.loginStatus.observe(this, Observer {
			when (it) {
				ACCEPTED -> navigateToConversations()
				REJECTED -> navigateToLogin()
			}
		})
	}

	private fun navigateToConversations() {
		if (findNavController().currentDestination?.id == R.id.waitFragment) {
			findNavController().navigate(SplashFragmentDirections.actionWaitFragmentToConversationsFragment())
		}
	}

	private fun navigateToLogin() {
		if (findNavController().currentDestination?.id == R.id.waitFragment) {
			findNavController().navigate(SplashFragmentDirections.actionWaitFragmentToLoginFragment())
		}
	}

	private val mViewModel by viewModels<WaitFragmentViewModel>()
}
