import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager
import com.broccolistefanipss.esamedazero.model.TrainingSession
import kotlinx.coroutines.launch

val sessionManager = SessionManager

class HomeViewModel(private val db: DB, private val userName: String) : ViewModel() {
    private val _trainingSessions = MutableLiveData<List<TrainingSession>>()
    val trainingSessions: LiveData<List<TrainingSession>> = _trainingSessions

    fun loadTrainingSessionsForUser() {
        viewModelScope.launch {
            _trainingSessions.value = db.getUserTrainingSessions(userName)
        }
    }
}