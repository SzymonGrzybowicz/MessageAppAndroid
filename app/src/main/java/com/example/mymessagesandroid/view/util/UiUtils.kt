package com.example.mymessagesandroid.view.util

import android.app.Activity
import android.content.Context
import android.util.Patterns
import android.view.inputmethod.InputMethodManager

class UiUtils {
	companion object {

		fun hideKeyboard(activity: Activity) {
			val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
		}

		fun isValidEmail(target: CharSequence): Boolean {
			return Patterns.EMAIL_ADDRESS.matcher(target).matches()
		}

	}
}