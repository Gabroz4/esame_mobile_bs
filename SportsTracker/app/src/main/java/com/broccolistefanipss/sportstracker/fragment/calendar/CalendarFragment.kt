package com.broccolistefanipss.sportstracker.fragment.calendar

import CalendarViewModel
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.broccolistefanipss.sportstracker.databinding.FragmentCalendarBinding
import com.broccolistefanipss.sportstracker.global.DB
import java.time.LocalDate
import java.time.format.DateTimeParseException

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarViewModel: CalendarViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        calendarViewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        val db = DB(requireContext())
        calendarViewModel.init(db)

        sharedPreferences = requireContext().getSharedPreferences("TrainingPrefs", Context.MODE_PRIVATE)

        setupCalendarClickListener()
        setupSaveButtonListener()

        observeTrainingSessions()

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeTrainingSessions() {
        calendarViewModel.trainingSessions.observe(viewLifecycleOwner) { sessions ->
            val calendarDays = sessions.mapNotNull {
                try {
                    LocalDate.parse(it.sessionDate)
                } catch (e: DateTimeParseException) {
                    null
                }
            }.toSet()
            binding?.calendarView?.addDecorator(EventDecorator(calendarDays))
        }
    }

    private fun setupCalendarClickListener() {
        binding?.calendarView?.setOnDateChangedListener { widget, date, selected ->
            val formattedDate = String.format("%04d-%02d-%02d", date.year, date.month + 1, date.day)
            binding?.editTextDate?.setText(formattedDate)
            checkForExistingTraining(formattedDate)
        }
    }

    private fun setupSaveButtonListener() {
        binding?.buttonSaveTraining?.setOnClickListener {
            val date = binding?.editTextDate?.text.toString()
            val details = binding?.editTextDetails?.text.toString()

            if (date.isNotEmpty() && details.isNotEmpty()) {
                saveTrainingSession(date, details)
                Toast.makeText(requireContext(), "Allenamento salvato", Toast.LENGTH_SHORT).show()
                checkForExistingTraining(date)
            } else {
                Toast.makeText(requireContext(), "Compila tutti i campi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveTrainingSession(date: String, details: String) {
        with(sharedPreferences.edit()) {
            putString("training_$date", details)
            apply()
        }
    }

    private fun checkForExistingTraining(date: String) {
        val details = sharedPreferences.getString("training_$date", null)

        if (details != null) {
            binding?.textViewTrainingDetails?.apply {
                text = "Allenamento per $date: $details"
                visibility = View.VISIBLE
            }
        } else {
            binding?.textViewTrainingDetails?.visibility = View.GONE
        }
    }
}

