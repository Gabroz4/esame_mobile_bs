package com.broccolistefanipss.esamedazero.fragment.home

import HomeViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.broccolistefanipss.esamedazero.databinding.FragmentHomeBinding
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager
import com.broccolistefanipss.esamedazero.manager.SharedPrefs
import com.broccolistefanipss.esamedazero.model.TrainingSession
import com.broccolistefanipss.esamedazero.model.User

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        // Crea SessionManager con il contesto del Fragment
        val sessionManager = SessionManager(requireContext())
        // Ottieni il userName dal SessionManager
        val userName = sessionManager.userName ?: "" // Usa un valore di default o gestisci l'assenza del userName
        // Passa il DB e il userName alla Factory
        HomeViewModelFactory(DB(requireContext()), userName)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sessionManager = SessionManager(requireContext())
        val userName = sessionManager.userName ?: "NomeDefault"

        viewModel.trainingSessions.observe(viewLifecycleOwner) { sessions ->
            val displayText = buildDisplayText(sessions)
            binding.homeUserName.text = displayText
        }

        return root
    }

    private fun buildDisplayText(sessions: List<TrainingSession>): String {
        return sessions.joinToString(separator = "\n") { session ->
            "Data: ${session.sessionDate}, Durata: ${session.duration}, Tipo: ${session.trainingType}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
