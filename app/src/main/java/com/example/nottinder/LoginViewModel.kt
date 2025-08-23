package com.example.nottinder

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userDataSource: UserDataSource) : ViewModel() {

    fun verifyUser(username: String): Boolean = userDataSource.verifyUser(username)

}