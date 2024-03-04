package com.broccolistefanipss.esamedazero.fragment.home

import HomeViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.broccolistefanipss.esamedazero.global.DB

class HomeViewModelFactory(private val db: DB, private val userName: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(db, userName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
