package com.broccolistefanipss.esamedazero.fragment.home

import com.broccolistefanipss.esamedazero.adapter.TrainingSessionAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.broccolistefanipss.esamedazero.databinding.FragmentHomeBinding
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager
import com.broccolistefanipss.esamedazero.model.TrainingSession

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var db: DB
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionManager = SessionManager(requireContext())
        val userName = sessionManager.userName ?: ""
        db = DB(requireContext())

        // Inizializza il ViewModel
        viewModel.initialize(requireContext(), userName)

        // Imposta il RecyclerView
        binding.trainingSessionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // stampa delle sessioni di allenamento
        viewModel.trainingSessions.observe(viewLifecycleOwner, Observer { sessions ->
            updateUI(sessions)
        })
    }

    private fun deleteSession(sessionId: Int) {
        val success = db.deleteTrainingSession(sessionId) // Chiama il metodo per eliminare dal DB
        if (success) {
            Toast.makeText(requireContext(), "Sessione eliminata con successo", Toast.LENGTH_SHORT).show()
            viewModel.loadTrainingSessions() // Ricarica le sessioni aggiornate
        } else {
            Toast.makeText(requireContext(), "Errore nell'eliminazione della sessione", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(sessions: List<TrainingSession>) {
        if (sessions.isNotEmpty()) {
            binding.trainingSessionsRecyclerView.visibility = View.VISIBLE
            binding.emptyTextView.visibility = View.GONE

            // Crea l'adapter e passa la funzione di eliminazione
            val adapter = TrainingSessionAdapter(sessions) { sessionId ->
                deleteSession(sessionId) // Chiama la funzione di eliminazione
            }
            binding.trainingSessionsRecyclerView.adapter = adapter
        } else {
            binding.trainingSessionsRecyclerView.visibility = View.GONE
            binding.emptyTextView.visibility = View.VISIBLE
            binding.emptyTextView.text = "Nessun allenamento registrato"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}