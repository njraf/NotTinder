package com.example.nottinder

import android.net.Uri
import androidx.collection.intListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataSource @Inject constructor() {

    val nullCandidate = User(-1, "", "", emptyList())

    // mock database. do not delete.
    private val _users: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    fun getUsers(): Flow<List<User>> = _users.asStateFlow()

    private var _self: MutableStateFlow<User?> = MutableStateFlow(null)
    fun getSelf(): Flow<User?> = _self.asStateFlow()

/*
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
    }*/

    fun verifyUser(username: String): Boolean {
        return _users.value.map { it.name }.contains(username)
    }

    fun updateSelf(newSelf: User) {
        val mutableUsers = _users.value.toMutableList()
        mutableUsers.remove(_self.value)
        mutableUsers.add(0, newSelf)
        _self.value = newSelf
        _users.value = mutableUsers
    }

    fun resetData() {
        _self.value = null
    }

    fun setCurrentUser(username: String) {
        val currentUser = _users.value.find { it.name == username } ?: return
        updateSelf(currentUser)
    }
}