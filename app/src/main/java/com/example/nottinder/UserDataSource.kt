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

    fun verifyUser(username: String): Boolean {
        return _users.value.map { it.name }.contains(username)
    }

    fun updateSelf(newSelf: User) {
        val mutableUsers = _users.value.toMutableList()
        val newSelfCopy =
            newSelf.copy(
                id = if (newSelf.id == -1)
                    (_users.value.maxOfOrNull { it.id } ?: -1) + 1
                else newSelf.id)
        mutableUsers.remove(newSelfCopy)
        mutableUsers.add(0, newSelfCopy)
        _self.value = newSelfCopy
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