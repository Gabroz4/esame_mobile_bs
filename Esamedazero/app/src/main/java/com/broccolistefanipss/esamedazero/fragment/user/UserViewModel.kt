package com.broccolistefanipss.esamedazero.fragment.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is User Fragment"
    }
    val text: LiveData<String> = _text
}