import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager
import com.broccolistefanipss.esamedazero.model.TrainingSession
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class CalendarViewModel() : ViewModel() { // Default constructor added

    private lateinit var db: DB
    private var contextRef: WeakReference<Context>? = null

    private val _trainingSessions = MutableLiveData<List<TrainingSession>>()
    val trainingSessions: LiveData<List<TrainingSession>> = _trainingSessions

    fun init(db: DB) { // Take DB for initialization
        this.db = db
        loadTrainingSessions()
    }

    private fun loadTrainingSessions() {
        viewModelScope.launch {
            val sessionManager = contextRef?.get()?.let { SessionManager(it) } // Safe usage of contextRef
            val userName = sessionManager?.userName ?: ""
            val sessions: List<TrainingSession> = db.getUserTrainingSessions(userName)
            _trainingSessions.postValue(sessions)
        }
    }
}
