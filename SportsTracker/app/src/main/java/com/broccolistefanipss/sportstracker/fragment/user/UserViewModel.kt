package com.broccolistefanipss.sportstracker.fragment.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.broccolistefanipss.sportstracker.global.DB
import com.broccolistefanipss.sportstracker.manager.SessionManager
import com.broccolistefanipss.sportstracker.model.User

class UserViewModel : ViewModel() {
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun loadUserData(database: DB, sessionManager: SessionManager) {
        val userName = sessionManager.userName ?: return
        _user.value = database.getUserData(userName)
    }
}
