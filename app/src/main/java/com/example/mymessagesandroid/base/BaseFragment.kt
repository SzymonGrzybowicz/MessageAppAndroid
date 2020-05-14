package com.example.mymessagesandroid.base

import androidx.fragment.app.Fragment
import com.example.mymessagesandroid.view.activity.MainActivity

abstract class BaseFragment: Fragment(){

	abstract fun getTitle(): String
	abstract val withToolbar: Boolean

	override fun onStart() {
		super.onStart()
		(activity as MainActivity).setActionBarText(getTitle())
		(activity as MainActivity).setActionBarVisibility(withToolbar)
	}
}