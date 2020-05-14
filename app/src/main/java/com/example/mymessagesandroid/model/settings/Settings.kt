package com.example.mymessagesandroid.model.settings

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.mymessagesandroid.model.dto.User

class Settings(private val context: Context) {


	var currentUser: User?
		get() {
			val id = readLong(USER_ID_KEY)
			if (id == -1L) {
				return null
			}
			return User(id, readString(USER_NAME_KEY), readString(USER_PASSWORD_KEY), readString(USER_MAIL_KEY))
		}
		set(value) {
			saveSetting(USER_ID_KEY, value?.id ?: -1)
			saveSetting(USER_NAME_KEY, value?.name ?: "")
			saveSetting(USER_PASSWORD_KEY, value?.password ?: "")
			saveSetting(USER_MAIL_KEY, value?.mail ?: "")
		}

	private fun saveSetting(key: String, value: String) {
		val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
		editor.putString(key, value)
		editor.apply()
	}

	private fun saveSetting(key: String, value: Boolean) {
		val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
		editor.putBoolean(key, value)
		editor.apply()
	}

	private fun saveSetting(key: String, value: Int) {
		val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
		editor.putInt(key, value)
		editor.apply()
	}

	private fun saveSetting(key: String, value: Long) {
		val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
		editor.putLong(key, value)
		editor.apply()
	}

	private fun readLong(key: String): Long {
		return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, -1)
	}

	private fun readString(key: String): String {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "") ?: ""
	}

	private fun readBoolean(key: String): Boolean {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false)
	}

	private fun readInt(key: String): Int {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, -1)
	}

	companion object {
		private const val USER_ID_KEY = "USER_ID"
		private const val USER_NAME_KEY = "USER_NAME"
		private const val USER_PASSWORD_KEY = "USER_PASSWORD"
		private const val USER_MAIL_KEY = "USER_MAIL"
	}
}