package com.example.mymessagesandroid.model.application

import android.app.Application
import android.media.RingtoneManager
import android.net.Uri
import com.example.mymessagesandroid.model.settings.Settings
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class MessagesApplication: Application() {

	override fun onCreate() {
		super.onCreate()
		settings = Settings(applicationContext)
		pushObservable.subscribe{
			try {
				val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
				val r = RingtoneManager.getRingtone(applicationContext, notification)
				r.play()
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
	}

	companion object {
		val mPushSubjectPublish: PublishSubject<String> = PublishSubject.create<String>()
		val pushObservable: Observable<String>
			get() = mPushSubjectPublish

		lateinit var settings: Settings
		private set
	}
}