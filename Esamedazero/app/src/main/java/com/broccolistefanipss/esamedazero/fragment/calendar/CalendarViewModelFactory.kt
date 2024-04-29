package com.broccolistefanipss.esamedazero.fragment.calendar

import CalendarViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.broccolistefanipss.esamedazero.global.DB

class CalendarViewModelFactory(private val db: DB) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}