package com.example.nottinder

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val userDataSource: UserDataSource) : ViewModel() {


    fun logOut() {
        userDataSource.resetData()
    }
}