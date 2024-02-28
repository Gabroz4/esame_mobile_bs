package com.broccolistefanipss.esamedazero.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.model.User
import kotlinx.coroutines.launch

class HomeViewModel(private val db: DB) : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _users.value = db.getData()
        }
    }
}
