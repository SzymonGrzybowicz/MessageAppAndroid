package com.example.mymessagesandroid.model.sg_controllers

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mymessagesandroid.BuildConfig
import com.example.mymessagesandroid.model.controler_interface.IMessageController
import com.example.mymessagesandroid.model.dto.Message
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

class MessageController: IMessageController {

	override val messagesData: LiveData<List<Message>>
		get() = mMessagesData


	override fun sentMessage(message: Message) {

		val url = HttpUrl.Builder()
			.scheme("https")
			.host(BuildConfig.SERVER_URL)
			.addPathSegment("message")
			.build()

		val gson = GsonBuilder().setPrettyPrinting().create()
		val json: String = gson.toJson(message)

		val request = Request.Builder()
			.url(url)
			.method("POST", RequestBody.create(MediaType.parse("application/json"), json))
			.build()

		okHttpClient.newCall(request).enqueue(object : Callback {
			override fun onFailure(request: Request?, e: IOException?) {
				Log.d(" MessageController", "onFailure()")
			}

			override fun onResponse(response: Response?) {
				Log.d(" MessageController", "response($response) onResponse()")
				//todo
			}
		})
	}

	override fun fetchMessages(conversationId: Long) {
		val url = HttpUrl.Builder()
			.scheme("https")
			.host(BuildConfig.SERVER_URL)
			.addPathSegment("message")
			.addPathSegment(conversationId.toString())
			.build()

		val request = Request.Builder()
			.url(url)
			.method("GET", null)
			.build()

		okHttpClient.newCall(request).enqueue(object : Callback {
			override fun onFailure(request: Request?, e: IOException?) {
				Log.d("MessageController", "onFailure()")
			}

			override fun onResponse(response: Response?) {
				Log.d("MessageController", "response($response) onResponse()")
				val status = response?.code()
				var conversations: List<Message>? = null
				if (status == 200) {
					val string = response.body().string()
					conversations = Gson().fromJson<Array<Message>>(string, Array<Message>::class.java).toList()
				}
				Handler(Looper.getMainLooper()).post {
					conversations?.let {
						mMessagesData.value = it
					}
				}
			}
		})
	}

	private val okHttpClient = OkHttpClient()
	private val mMessagesData = MutableLiveData<List<Message>>()
}