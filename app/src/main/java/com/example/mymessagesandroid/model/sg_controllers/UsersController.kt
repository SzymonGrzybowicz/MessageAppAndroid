package com.example.mymessagesandroid.model.sg_controllers

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mymessagesandroid.BuildConfig
import com.example.mymessagesandroid.model.controler_interface.IUsersController
import com.example.mymessagesandroid.model.dto.User
import com.google.gson.Gson
import com.squareup.okhttp.Callback
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException

class UsersController : IUsersController {

	override fun fetchUsers() {
		val url = HttpUrl.Builder()
			.scheme("https")
			.host(BuildConfig.SERVER_URL)
			.addPathSegment("user")
			.addPathSegment("all")
			.build()

		val request = Request.Builder()
			.url(url)
			.method("GET", null)
			.build()

		okHttpClient.newCall(request).enqueue(object : Callback {
			override fun onFailure(request: Request?, e: IOException?) {
				Log.d("LoginController", "onFailure()")
				Handler(Looper.getMainLooper()).post {
					mUserData.value = listOf()
				}
			}

			override fun onResponse(response: Response?) {
				Log.d("LoginController", "response($response) onResponse()")
				val status = response?.code()
				var Users: List<User> = listOf()
				if (status == 200) {
					Users = Gson().fromJson<Array<User>>(response.body().string(), Array<User>::class.java).toList()
				}
				Handler(Looper.getMainLooper()).post {
					mUserData.value = Users
				}
			}
		})
	}

	override val usersData: LiveData<List<User>>
		get() = mUserData

	private val mUserData = MutableLiveData<List<User>>()
	private val okHttpClient = OkHttpClient()
}