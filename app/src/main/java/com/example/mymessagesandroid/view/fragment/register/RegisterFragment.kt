package com.example.mymessagesandroid.view.fragment.register

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mymessagesandroid.R
import com.example.mymessagesandroid.base.BaseFragment
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status.ACCEPTED
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status.IN_PROGRESS
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status.REJECTED
import com.example.mymessagesandroid.model.dto.User
import com.example.mymessagesandroid.view.util.UiUtils
import com.example.mymessagesandroid.view_model.fragment.RegisterFragmentViewModel
import kotlinx.android.synthetic.main.fragment_login.register_btn
import kotlinx.android.synthetic.main.fragment_register.register_confirm_password_edit_text
import kotlinx.android.synthetic.main.fragment_register.register_mail_edit_text
import kotlinx.android.synthetic.main.fragment_register.register_message_txt
import kotlinx.android.synthetic.main.fragment_register.register_name_edit_text
import kotlinx.android.synthetic.main.fragment_register.register_password_edit_text

class RegisterFragment: BaseFragment() {

	override fun getTitle(): String {
		return resources.getString(R.string.register)
	}

	override val withToolbar: Boolean
		get() = true

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		return inflater.inflate(R.layout.fragment_register, null)
	}

	override fun onStart() {
		super.onStart()
		register_btn.setOnClickListener {
			val name = register_name_edit_text.text.toString()
			val mail = register_mail_edit_text.text.toString()
			val password = register_password_edit_text.text.toString()
			val confirmPassword = register_confirm_password_edit_text.text.toString()

			if (name.isEmpty()) {
				register_message_txt.setText(R.string.add_name)
				return@setOnClickListener
			}

			if (mail.isEmpty()) {
				register_message_txt.setText(R.string.add_mail)
				return@setOnClickListener
			} else if (!UiUtils.isValidEmail(mail)) {
				register_message_txt.setText(R.string.mail_invalid)
				return@setOnClickListener
			}

			if (password.isEmpty()) {
				register_message_txt.setText(R.string.add_password)
				return@setOnClickListener
			}

			if (password != confirmPassword) {
				register_message_txt.setText(R.string.passwords_not_the_same)
				return@setOnClickListener
			}

			mViewModel.registerUser(User(null, name, password, mail))
		}

		mViewModel.registrationStatus.observe(this, Observer { status ->
			when (status) {
				REJECTED -> {
					register_message_txt.setText(R.string.try_another_mail)
					register_btn.isEnabled = true
				}
				IN_PROGRESS -> {
					register_message_txt.setText(R.string.wait)
					register_btn.isEnabled = false
				}
				ACCEPTED -> {
					if (findNavController().currentDestination?.id == R.id.registerFragment) {
						findNavController().popBackStack()
					}
				}
			}
		})
	}

	private val mViewModel by viewModels<RegisterFragmentViewModel>()
}