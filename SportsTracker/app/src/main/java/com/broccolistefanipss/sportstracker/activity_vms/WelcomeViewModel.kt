package com.broccolistefanipss.sportstracker.activity_vms

//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.broccolistefanipss.sportstracker.global.DB
//import com.broccolistefanipss.sportstracker.manager.SessionManager
//
//class WelcomeViewModel(application: Application) : AndroidViewModel(application) {
//
//    private val sessionManager by lazy { SessionManager(getApplication()) }
//    private val db by lazy { DB(getApplication()) }
//
//    private val _navigateToLogin = MutableLiveData<Unit>()
//    val navigateToLogin: LiveData<Unit> = _navigateToLogin
//
//    private val _showError = MutableLiveData<String>()
//    val showError: LiveData<String> = _showError
//
//    fun isUserLoggedIn(): Boolean {
//        return sessionManager.isLoggedIn
//    }
//
//    fun saveUserData(userName: String, password: String, sesso: String, eta: Int, altezza: Int, peso: Int, objective: String) {
//        val result = db.insertUser(userName, password, sesso, eta, altezza, peso, objective)
//
//        if (result) {
//            sessionManager.setLogin(true)
//            sessionManager.userName = userName
//            _navigateToLogin.value = Unit
//        } else {
//            _showError.value = "Questo UserName esiste già"
//        }
//    }
//}