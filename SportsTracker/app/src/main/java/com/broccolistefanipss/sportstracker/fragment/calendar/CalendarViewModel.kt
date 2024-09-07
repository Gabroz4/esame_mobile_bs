import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broccolistefanipss.sportstracker.global.DB
import com.broccolistefanipss.sportstracker.manager.SessionManager
import com.broccolistefanipss.sportstracker.model.CalendarTraining
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class CalendarViewModel : ViewModel() {

    private lateinit var db: DB
    private var contextRef: WeakReference<Context>? = null

    private val _calendarTrainingSessions = MutableLiveData<List<CalendarTraining>>()
    val calendarTrainingSessions: LiveData<List<CalendarTraining>> = _calendarTrainingSessions

    // Inizializza viewmodel con db e context
    fun init(db: DB, context: Context) {
        this.db = db
        this.contextRef = WeakReference(context)
        loadCalendarTrainingsUser()
    }

    // Carica allenamenti per utente corrente
    private fun loadCalendarTrainingsUser() {
        viewModelScope.launch {
            val sessionManager = contextRef?.get()?.let { SessionManager(it) }
            val userName = sessionManager?.userName ?: ""
            val sessions: List<CalendarTraining> = db.getCalendarTrainingsUser(userName)
            _calendarTrainingSessions.postValue(sessions)

            sessions.forEach { session ->
                Log.d("CalendarTraining", "UserName: ${session.userName}")
                Log.d("CalendarTraining", "Date: ${session.date}")
                Log.d("CalendarTraining", "Description: ${session.description}")
            }
        }
    }

    // Salva trainingSession nel database e ricarica
    fun saveCalendarTraining(date: String, description: String) {
        viewModelScope.launch {
            val sessionManager = contextRef?.get()?.let { SessionManager(it) }
            val userName = sessionManager?.userName ?: ""
            db.insertCalendarTraining(userName, date, description)
            loadCalendarTrainingsUser()  // Ricarica
        }
    }
}