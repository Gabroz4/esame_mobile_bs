import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager
import com.broccolistefanipss.esamedazero.model.TrainingSession
import kotlinx.coroutines.launch

val sessionManager = SessionManager

// Definisce la classe HomeViewModel che estende ViewModel.
// ViewModel fornisce i dati per l'UI e sopravvive ai cambiamenti di configurazione come rotazioni dello schermo.
class HomeViewModel(private val db: DB, private val userName: String) : ViewModel() {
    private val _trainingSessions = MutableLiveData<List<TrainingSession>>()
    val trainingSessions: LiveData<List<TrainingSession>> = _trainingSessions

    fun loadTrainingSessionsForUser() {
        viewModelScope.launch {
            // Qui si assume che il metodo db.getUserTrainingSessions(userName) restituisca una lista di oggetti TrainingSession
            // per l'utente specificato dal campo userName del ViewModel.
            val sessions = db.getUserTrainingSessions(userName)
            // Utilizza postValue per assegnare i dati a _trainingSessions su un thread non principale.
            _trainingSessions.postValue(sessions)
        }
    }
}