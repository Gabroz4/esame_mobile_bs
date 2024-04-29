package com.broccolistefanipss.esamedazero.fragment.calendar

import CalendarViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.broccolistefanipss.esamedazero.databinding.FragmentCalendarBinding
import com.broccolistefanipss.esamedazero.global.DB
import java.time.LocalDate
class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        calendarViewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        // Assuming you initialize your DB instance at some point
        val db = DB(requireContext())
        calendarViewModel.init(db) // Initialize the ViewModel with the DB

        observeTrainingSessions()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeTrainingSessions() {
        calendarViewModel.trainingSessions.observe(viewLifecycleOwner) { sessions ->
            val calendarDays = sessions.mapNotNull { LocalDate.parse(it.sessionDate) }.toSet()
            binding.calendarView.addDecorator(EventDecorator(calendarDays))
        }
    }
}