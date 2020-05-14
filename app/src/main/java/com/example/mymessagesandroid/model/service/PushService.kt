package com.example.mymessagesandroid.model.service

import android.util.Log
import com.example.mymessagesandroid.model.application.MessagesApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushService: FirebaseMessagingService() {

		override fun onNewToken(s: String) {
			super.onNewToken(s)
		}

		override fun onMessageReceived(remoteMessage: RemoteMessage) {
			Log.d(TAG, "messageData(${remoteMessage.data}) onMessageReceived()")
			MessagesApplication.mPushSubjectPublish.onNext("")
		}

	private val TAG = "PushService"
}