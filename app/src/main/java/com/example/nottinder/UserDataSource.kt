package com.example.nottinder

import android.net.Uri
import androidx.collection.intListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataSource @Inject constructor() {

    private val _users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    fun updateName(id: Int, newName: String) {
        val userList = _users.value.toMutableList()
        if (id !in userList.map { it.id }) {
            userList.add(0, User(id, newName, "", emptyList()))
        } else {
            val targetUser: User = userList.find { it.id == id }!!.copy(name = newName)
            val targetIndex = userList.indexOfFirst { it.id == id }
            userList[targetIndex] = targetUser
        }
        _users.value = userList
    }

    fun updateBio(id: Int, newBio: String) {
        val userList = _users.value.toMutableList()
        if (id !in userList.map { it.id }) {
            userList.add(0, User(id, "", newBio, emptyList()))
        } else {
            val targetUser: User = userList.find { it.id == id }!!.copy(biography = newBio)
            val targetIndex = userList.indexOfFirst { it.id == id }
            userList[targetIndex] = targetUser
        }
        _users.value = userList
    }

    fun updatePhotos(id: Int, photos: List<Uri>) {
        val userList = _users.value.toMutableList()
        if (id !in userList.map { it.id }) {
            userList.add(0, User(id, "Nick", "", photos))
        } else {
            val targetUser: User = userList.find { it.id == id }!!.copy(pictureUris = photos)
            val targetIndex = userList.indexOfFirst { it.id == id }
            userList[targetIndex] = targetUser
        }
        _users.value = userList
    }

    fun verifyUser(username: String): Boolean {
        return users.value.map { it.name }.contains(username)
    }
}