package com.broccolistefanipss.esamedazero.fragment.home

import HomeViewModel
import TrainingSessionAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.broccolistefanipss.esamedazero.databinding.FragmentHomeBinding
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager
import com.broccolistefanipss.esamedazero.manager.SharedPrefs
import com.broccolistefanipss.esamedazero.model.TrainingSession
import com.broccolistefanipss.esamedazero.model.User


// Fragment che visualizza la home dell'applicazione, inclusi gli allenamenti dell'utente.
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        // Ottiene il nome utente dal SessionManager e passa DB e userName alla Factory del ViewModel.
        val sessionManager = SessionManager(requireContext())
        val userName = sessionManager.userName ?: ""
        HomeViewModelFactory(DB(requireContext()), userName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        // Osserva i dati delle sessioni di allenamento dal ViewModel e aggiorna il RecyclerView.
        viewModel.trainingSessions.observe(viewLifecycleOwner) { sessions ->
            updateRecyclerView(sessions)
        }
    }

    private fun setupRecyclerView() {
        binding.trainingSessionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TrainingSessionAdapter(emptyList()) // Imposta un adapter iniziale vuoto.
        }
    }

    private fun updateRecyclerView(sessions: List<TrainingSession>) {
        // Ottiene l'adapter dal RecyclerView e aggiorna i dati.
        (binding.trainingSessionsRecyclerView.adapter as? TrainingSessionAdapter)?.let { adapter ->
            adapter.sessions = sessions
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
