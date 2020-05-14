package com.example.mymessagesandroid.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.mymessagesandroid.R
import com.example.mymessagesandroid.model.application.MessagesApplication
import com.example.mymessagesandroid.view_model.activity.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.main_activity_toolbar_menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when(item.itemId){
			android.R.id.home-> {
				if (!findNavController(R.id.fragment).popBackStack()) {
					finish()
				}
			}
			R.id.menu_main_logout -> {
				MessagesApplication.settings.currentUser = null
				findNavController(R.id.fragment).navigate(R.id.loginFragment, null, NavOptions.Builder()
					.setPopUpTo(R.id.waitFragment,
						true).build())
			}
		}
		return super.onOptionsItemSelected(item)
	}

	override fun onStart() {
		super.onStart()
		window?.setSoftInputMode(
			WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
		)
	}

	fun setActionBarText(text: String) {
		supportActionBar?.title = text
	}

	fun setActionBarVisibility(visible: Boolean){
		if (visible) {
			supportActionBar?.show()
		} else {
			supportActionBar?.hide()
		}
	}

	private val mViewModel by viewModels<MainActivityViewModel>()
}
