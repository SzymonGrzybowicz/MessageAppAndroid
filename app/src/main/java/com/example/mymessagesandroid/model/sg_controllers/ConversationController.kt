package com.example.mymessagesandroid.model.sg_controllers

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mymessagesandroid.BuildConfig
import com.example.mymessagesandroid.model.application.MessagesApplication
import com.example.mymessagesandroid.model.controler_interface.IConversationController
import com.example.mymessagesandroid.model.dto.Conversation
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

class ConversationController : IConversationController {

	override fun fetchConversations(userId: Long) {

		val url = HttpUrl.Builder()
			.scheme("https")
			.host(BuildConfig.SERVER_URL)
			.addPathSegment("conversation")
			.addPathSegment(userId.toString())
			.build()

		val request = Request.Builder()
			.url(url)
			.method("GET", null)
			.build()

		okHttpClient.newCall(request).enqueue(object : Callback {
			override fun onFailure(request: Request?, e: IOException?) {
				Log.d("ConversationController", "onFailure()")
			}

			override fun onResponse(response: Response?) {
				Log.d("ConversationController", "response($response) onResponse()")
				val status = response?.code()
				var conversations: List<Conversation>? = null
				if (status == 200) {
					val string = response.body().string()
					conversations =
						Gson().fromJson<Array<Conversation>>(string, Array<Conversation>::class.java).toList()
				}
				Handler(Looper.getMainLooper()).post {
					conversations?.let {
						mConversationData.value = it
					}
				}
			}
		})
	}

	override fun createConversation(userId: Long, conversation: Conversation) {

		val url = HttpUrl.Builder()
			.scheme("https")
			.host(BuildConfig.SERVER_URL)
			.addPathSegment("conversation")
			.addPathSegment(userId.toString())
			.build()

		val gson = GsonBuilder().setPrettyPrinting().create()

		val json: String = gson.toJson(conversation)

		val request = Request.Builder()
			.url(url)
			.method("POST", RequestBody.create(MediaType.parse("application/json"), json))
			.build()

		okHttpClient.newCall(request).enqueue(object : Callback {
			override fun onFailure(request: Request?, e: IOException?) {
				Log.d("ConversationController", "onFailure()")
			}

			override fun onResponse(response: Response?) {
				Log.d("ConversationController", "response($response) onResponse()")
				MessagesApplication.settings.currentUser?.id?.let {
					fetchConversations(it)
				}
			}
		})
	}

	override fun markAsRead(conversationId: Long) {

		val url = HttpUrl.Builder()
			.scheme("https")
			.host(BuildConfig.SERVER_URL)
			.addPathSegment("conversation")
			.addPathSegment("markRead")
			.addPathSegment(conversationId.toString())
			.build()

		val request = Request.Builder()
			.url(url)
			.method("PATCH",  RequestBody.create(null, ByteArray(0)))
			.build()

		okHttpClient.newCall(request).enqueue(object : Callback {
			override fun onFailure(request: Request?, e: IOException?) {
				Log.d("ConversationController", "onFailure()")
			}

			override fun onResponse(response: Response?) {
				Log.d("ConversationController", "response($response) onResponse()")
			}
		})
	}

	override val conversationsData: LiveData<List<Conversation>>
		get() = mConversationData

	private val mConversationData = MutableLiveData<List<Conversation>>()
	private val okHttpClient = OkHttpClient()
}