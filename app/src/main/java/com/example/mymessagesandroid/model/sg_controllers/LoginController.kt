package com.example.mymessagesandroid.model.sg_controllers

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mymessagesandroid.BuildConfig
import com.example.mymessagesandroid.model.application.MessagesApplication
import com.example.mymessagesandroid.model.controler_interface.ILoginController
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status.ACCEPTED
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status.IN_PROGRESS
import com.example.mymessagesandroid.model.controler_interface.ILoginController.Status.REJECTED
import com.example.mymessagesandroid.model.dto.User
import com.example.mymessagesandroid.view.activity.MainActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.okhttp.Callback
import com.squareup.okhttp.HttpUrl
import com.squareup.okhttp.MediaType
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import com.squareup.okhttp.Response
import java.io.IOException

class LoginController : ILoginController {

	override fun login(user: User) {
		mLoginStatus.value = IN_PROGRESS

		val url = HttpUrl.Builder()
			.scheme("https")
			.host(BuildConfig.SERVER_URL)
			.addPathSegment("user")
			.addPathSegment("login")
			.build()

		val gson = GsonBuilder().setPrettyPrinting().create()

		val json: String = gson.toJson(user)

		val request = Request.Builder()
			.url(url)
			.method("POST", RequestBody.create(MediaType.parse("application/json"), json))
			.build()

		okHttpClient.newCall(request).enqueue(object : Callback {
			override fun onFailure(request: Request?, e: IOException?) {
				Log.d("LoginController", "onFailure()")
				Handler(Looper.getMainLooper()).post {
					mLoginStatus.value = REJECTED
				}
			}

			override fun onResponse(response: Response?) {
				Log.d("LoginController", "response($response) onResponse()")
				val status = response?.code()
				var loggedUser: User? = null
				if (status == 200) {
					loggedUser = Gson().fromJson<User?>(response.body().string(), User::class.java)
				}
				Handler(Looper.getMainLooper()).post {
					if (status == 200) {
						loggedUser?.password = user.password
						MessagesApplication.settings.currentUser = loggedUser
						mLoginStatus.value = ACCEPTED
					} else {
						mLoginStatus.value = REJECTED
					}
				}
			}
		})
	}

	override fun sentFirebaseAuthToken(userId: Long, authToken: String) {
		val url = HttpUrl.Builder()
			.scheme("https")
			.host(BuildConfig.SERVER_URL)
			.addPathSegment("user")
			.addPathSegment("firebase")
			.addPathSegment(userId.toString())
			.addPathSegment(authToken)
			.build()

		val request = Request.Builder()
			.url(url)
			.method("POST", RequestBody.create(null, ByteArray(0)))
			.build()

		okHttpClient.newCall(request).enqueue(object : Callback {
			override fun onFailure(request: Request?, e: IOException?) {
				Log.d("LoginController", "onFailure()")
			}

			override fun onResponse(response: Response?) {
				Log.d("LoginController", "response($response) onResponse()")
			}
		})
	}

	override fun registerUser(user: User) {
		mRegistrationStatus.value = IN_PROGRESS

		val url = HttpUrl.Builder()
			.scheme("https")
			.host(BuildConfig.SERVER_URL)
			.addPathSegment("user")
			.build()

		val gson = GsonBuilder().setPrettyPrinting().create()

		val json: String = gson.toJson(user)

		val request = Request.Builder()
			.url(url)
			.method("POST", RequestBody.create(MediaType.parse("application/json"), json))
			.build()

		okHttpClient.newCall(request).enqueue(object : Callback {
			override fun onFailure(request: Request?, e: IOException?) {
				Log.d("LoginController", "onFailure()")
				Handler(Looper.getMainLooper()).post {
					mLoginStatus.value = REJECTED
				}
			}

			override fun onResponse(response: Response?) {
				Log.d("LoginController", "response($response) onResponse()")
				Handler(Looper.getMainLooper()).post {
					val status = response?.code()
					if (status == 200) {
						mRegistrationStatus.value = ACCEPTED
					} else {
						mRegistrationStatus.value = REJECTED
					}
				}
			}
		})
	}


	override val registrationStatus: LiveData<Status>
		get() = mRegistrationStatus

	override val loginStatus: LiveData<Status>
		get() = mLoginStatus

	private val mLoginStatus = MutableLiveData<Status>()
	private val mRegistrationStatus = MutableLiveData<Status>()
	private val okHttpClient = OkHttpClient()
}