package com.example.mymessagesandroid.model.dto

data class User(
	val id: Long? = null,
	val name: String? = null,
	var password: String? = null,
	val mail: String? = null
){

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as User

		if (id != other.id) return false
		if (name != other.name) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id?.hashCode() ?: 0
		result = 31 * result + (name?.hashCode() ?: 0)
		return result
	}
}