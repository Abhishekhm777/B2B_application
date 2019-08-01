package com.example.compaq.b2b_application.Helper_classess

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

class UserInfoHelperClass :ViewModel() {
private val count :String = ""
    fun getUsers(): String {
        return count
    }

    private fun loadUsers() {

    }
}