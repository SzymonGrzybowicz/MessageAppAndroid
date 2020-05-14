package com.example.mymessagesandroid.view.fragment.coversations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.mymessagesandroid.R
import com.example.mymessagesandroid.base.BaseFragment
import com.example.mymessagesandroid.model.application.MessagesApplication
import com.example.mymessagesandroid.model.dto.Conversation
import com.example.mymessagesandroid.model.dto.User
import com.example.mymessagesandroid.view_model.fragment.ConversationsFragmentViewModel
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_conversations.add_conversation_btn
import kotlinx.android.synthetic.main.fragment_conversations.conversations_list
import kotlinx.android.synthetic.main.fragment_conversations.conversations_message_txt

class ConversationsFragment : BaseFragment() {

	override fun getTitle(): String {
		return resources.getString(R.string.conversations)
	}

	override val withToolbar: Boolean
		get() = true

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		super.onCreateView(inflater, container, savedInstanceState)
		return inflater.inflate(R.layout.fragment_conversations, null)
	}

	override fun onStart() {
		super.onStart()
		mPushDisposable = MessagesApplication.pushObservable.subscribe {
			mViewModel.fetchConversations(MessagesApplication.settings.currentUser?.id ?: -1)
		}
		conversations_message_txt.setText(R.string.wait)
		val settings = MessagesApplication.settings
		mViewModel.fetchConversations(settings.currentUser?.id ?: -1)
		mViewModel.conversations.observe(this, Observer {
			if (it.isEmpty()) {
				conversations_message_txt.setText(R.string.no_conversations)
				return@Observer
			}
			conversations_message_txt.visibility = View.GONE
			mConversations = it
			mAdapter = ConversationsAdapter(requireContext(), it)
			conversations_list.adapter = mAdapter
			conversations_list.setOnItemClickListener { _, _, position, _ ->
				if (findNavController().currentDestination?.id == R.id.conversationsFragment) {
					findNavController().navigate(
						ConversationsFragmentDirections.actionConversationsFragmentToChatFragment(
							mAdapter?.getItemId(position) ?: -1, mAdapter?.getName(position) ?: ""
						)
					)
				}
			}
		})

		add_conversation_btn.setOnClickListener {
			showUserSelectionDialog()
		}
	}

	override fun onStop() {
		super.onStop()
		mPushDisposable?.dispose()
		mDialog?.dismiss()
		mDialog = null
	}

	private fun showUserSelectionDialog() {
		val settings = MessagesApplication.settings
		mViewModel.fetchUsers()
		mViewModel.users.observe(this, Observer {
			mDialog?.let {
				return@Observer
			}
			context?.let { context ->
				val usersCanBeUsed = mutableListOf<User>()
				it.forEach { user ->
					var add = true
					if (user.mail == settings.currentUser?.mail) {
						add = false
					}
					mConversations?.forEach { conversation ->
						if (conversation.with.contains(user)) {
							add = false
						}
					}
					if (add) {
						usersCanBeUsed.add(user)
					}
				}
				val builder = AlertDialog.Builder(context)
				builder.setTitle(R.string.select_user)
				if (usersCanBeUsed.isNotEmpty()) {
					builder.setSingleChoiceItems(
						usersCanBeUsed.map { u -> u.name }.toTypedArray(),
						-1
					) { dialog, which ->
						dialog.dismiss()
						mViewModel.createConversation(settings.currentUser!!, usersCanBeUsed[which])
					}
				} else {
					builder.setMessage(R.string.no_new_users)
				}
				builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
					dialog.dismiss()
				}
				builder.setOnDismissListener {
					mDialog = null
				}
				mDialog = builder.show()
			}
		})
	}

	private val mViewModel by viewModels<ConversationsFragmentViewModel>()
	private var mDialog: AlertDialog? = null
	private var mConversations: List<Conversation>? = null
	private var mAdapter: ConversationsAdapter? = null
	private var mPushDisposable: Disposable? = null
}