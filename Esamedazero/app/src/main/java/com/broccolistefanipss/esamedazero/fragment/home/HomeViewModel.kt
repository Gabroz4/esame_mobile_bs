package com.broccolistefanipss.esamedazero.fragment.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.model.TrainingSession
import com.broccolistefanipss.esamedazero.model.User
import kotlinx.coroutines.launch

class HomeViewModel(private val db: DB) : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users
    private val _trainings = MutableLiveData<List<TrainingSession>>()
    val trainings: LiveData<List<TrainingSession>> = _trainings


    init {
        loadUsers()
        //loadTrainings()
    }

   // private fun loadUser(userName: String?) {
   //     val userName = db.isN
   // }
    private fun loadUsers() {
        viewModelScope.launch {
            _users.value = db.getUsers()

        }
    }

    //private fun loadTrainings() {
    //    viewModelScope.launch {
    //        _users.value = db.getUserTrainingSessions()
//
    //    }
    //}
}
