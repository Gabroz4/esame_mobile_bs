package com.broccolistefanipss.sportstracker.fragment.home

import com.broccolistefanipss.sportstracker.adapter.TrainingSessionAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.broccolistefanipss.sportstracker.R
import com.broccolistefanipss.sportstracker.databinding.FragmentHomeBinding
import com.broccolistefanipss.sportstracker.manager.SessionManager
import com.broccolistefanipss.sportstracker.model.TrainingSession

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionManager = SessionManager(requireContext())
        val userName = sessionManager.userName ?: ""
        viewModel.initialize(requireContext(), userName)

        binding.trainingSessionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.trainingSessions.observe(viewLifecycleOwner) { sessions ->
            updateUI(sessions)
        }
    }
    private fun deleteSession(sessionId: Int) {
        viewModel.deleteSession(sessionId)
    }

    private fun updateUI(sessions: List<TrainingSession>) {
        if (sessions.isNotEmpty()) {
            binding.trainingSessionsRecyclerView.visibility = View.VISIBLE
            binding.emptyTextView.visibility = View.GONE

            // crea adapter e passa la funzione di eliminazione
            val adapter = TrainingSessionAdapter(requireContext(), sessions) { sessionId ->
                deleteSession(sessionId) // chiama la funzione di eliminazione,
            }
            binding.trainingSessionsRecyclerView.adapter = adapter
        } else {
            binding.trainingSessionsRecyclerView.visibility = View.GONE
            binding.emptyTextView.visibility = View.VISIBLE
            binding.emptyTextView.text = getString(R.string.no_allenamenti)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}