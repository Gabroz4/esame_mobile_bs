package com.broccolistefanipss.esamedazero.fragment.home

import HomeViewModel
import com.broccolistefanipss.esamedazero.adapter.TrainingSessionAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        // Inizializza il ViewModel
        viewModel.initialize(requireContext(), userName)

        // Imposta il RecyclerView
        binding.trainingSessionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Osserva le sessioni di allenamento
        viewModel.trainingSessions.observe(viewLifecycleOwner, Observer { sessions ->
            if (sessions.isNotEmpty()) {
                binding.trainingSessionsRecyclerView.adapter = TrainingSessionAdapter(sessions)
                binding.emptyTextView.visibility = View.GONE
            } else {
                binding.emptyTextView.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Definizione del Fragment che rappresenta la schermata principale dell'app.
//class HomeFragment : Fragment() {
//
//    // Variabile per il view binding, permette di accedere facilmente agli elementi della view.
//    private var _binding: FragmentHomeBinding? = null
//    // Getter per _binding che assicura che il binding sia non-null quando acceduto.
//    private val binding get() = _binding!!
//
//    // Lazy initialization del ViewModel. Viene utilizzata una factory per passare dipendenze specifiche al ViewModel.
//    private val viewModel: HomeViewModel by viewModels()
//
//    // Metodo chiamato quando il Fragment deve creare la sua view.
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        // Inflazione del layout del Fragment utilizzando view binding.
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    // Metodo chiamato immediatamente dopo che onCreateView() ha completato la creazione della view.
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        // Configura il RecyclerView.
//        setupRecyclerView()
//
//        // Osserva il LiveData contenuto nel ViewModel per ottenere le sessioni di allenamento e aggiornare la UI di conseguenza.
//        viewModel.trainingSessions.observe(viewLifecycleOwner) { sessions ->
//            updateRecyclerView(sessions)
//        }
//    }
//
//    // Configura il RecyclerView, impostando il suo layout manager e l'adapter.
//    private fun setupRecyclerView() {
//        binding.trainingSessionsRecyclerView.apply {
//            layoutManager = LinearLayoutManager(context) // Imposta un LinearLayoutManager.
//            adapter = TrainingSessionAdapter(emptyList()) // Inizializza l'adapter con una lista vuota.
//        }
//    }
//
//    // Aggiorna i dati visualizzati nel RecyclerView.
//    private fun updateRecyclerView(sessions: List<TrainingSession>) {
//        // Ottiene l'adapter dal RecyclerView e aggiorna i dati se l'adapter è correttamente castato.
//        (binding.trainingSessionsRecyclerView.adapter as? TrainingSessionAdapter)?.let { adapter ->
//            adapter.sessions = sessions // Aggiorna i dati dell'adapter.
//            adapter.notifyDataSetChanged() // Notifica che i dati sono cambiati.
//        }
//    }
//
//    // Metodo chiamato quando la view del Fragment viene distrutta.
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null // Pulisce il riferimento al binding per evitare memory leaks.
//    }
//}
