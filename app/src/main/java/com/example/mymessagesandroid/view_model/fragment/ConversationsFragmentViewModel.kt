package com.example.mymessagesandroid.view_model.fragment

import androidx.lifecycle.ViewModel
import com.example.mymessagesandroid.model.controler_interface.IConversationController
import com.example.mymessagesandroid.model.controler_interface.IUsersController
import com.example.mymessagesandroid.model.dto.Conversation
import com.example.mymessagesandroid.model.dto.User
import com.example.mymessagesandroid.model.sg_controllers.ConversationController
import com.example.mymessagesandroid.model.sg_controllers.UsersController

class ConversationsFragmentViewModel(
	private val mConversationController: IConversationController = ConversationController(),
	private val mUsersController: IUsersController = UsersController()
) : ViewModel() {


	fun fetchConversations(userId: Long) {
		mConversationController.fetchConversations(userId)
	}

	fun fetchUsers() {
		mUsersController.fetchUsers()
	}

	fun createConversation(creator: User, member: User) {
		creator.id?.let {
			mConversationController.createConversation(it, Conversation(null, listOf(member), false))
		}
	}
	val conversations = mConversationController.conversationsData
	val users = mUsersController.usersData
}