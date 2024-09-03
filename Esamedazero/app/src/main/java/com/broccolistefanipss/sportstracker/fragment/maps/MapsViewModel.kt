package com.broccolistefanipss.sportstracker.fragment.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


//lo lasciamo per il meme
class MapsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is maps Fragment"
    }
    val text: LiveData<String> = _text
}