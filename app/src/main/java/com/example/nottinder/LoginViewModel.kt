package com.example.nottinder

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userDataSource: UserDataSource) : ViewModel() {

    fun verifyUser(username: String) {

    }
}