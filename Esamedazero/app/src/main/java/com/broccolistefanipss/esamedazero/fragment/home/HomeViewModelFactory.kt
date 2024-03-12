package com.broccolistefanipss.esamedazero.fragment.home

import HomeViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.broccolistefanipss.esamedazero.global.DB

// Definizione della classe HomeViewModelFactory che implementa ViewModelProvider.Factory.
// Questo permette di creare ViewModel con parametri nel costruttore.
class HomeViewModelFactory(private val db: DB, private val userName: String) : ViewModelProvider.Factory {

    // Override del metodo create per generare istanze di ViewModel.
    // Questo metodo è richiamato quando un ViewModel è richiesto da un componente dell'app.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Controlla se il tipo di ViewModel richiesto è assegnabile da HomeViewModel,
        // il che significa che può essere un HomeViewModel o una sua sottoclasse.
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            // Se è vero, crea un'istanza di HomeViewModel passando le dipendenze necessarie (db e userName).
            // 'as T' effettua un cast sicuro dell'istanza a T, il tipo di ViewModel richiesto.
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(db, userName) as T
        }
        // Se il tipo richiesto non corrisponde, lancia un'eccezione.
        // Questo previene l'istanziazione di un ViewModel di tipo non previsto dalla factory.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
