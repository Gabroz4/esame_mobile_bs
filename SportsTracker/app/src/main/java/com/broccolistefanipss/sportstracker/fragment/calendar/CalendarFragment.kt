package com.broccolistefanipss.sportstracker.fragment.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.broccolistefanipss.sportstracker.R
import com.broccolistefanipss.sportstracker.databinding.FragmentCalendarBinding
import com.broccolistefanipss.sportstracker.global.DB
import java.time.LocalDate
import java.time.format.DateTimeParseException

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

        val db = DB(requireContext())
        calendarViewModel.init(db, requireContext())

        setupCalendarClickListener()
        setupSaveButtonListener()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCalendarClickListener() {
        binding.calendarView.setOnDateChangedListener { _, date, _ ->
            val formattedDate = String.format(
                "%02d-%02d-%04d",
                date.day,
                date.month + 1,
                date.year
            )
            binding.editTextDate.setText(formattedDate)
            checkForExistingTraining(formattedDate)
        }
    }

    private fun setupSaveButtonListener() {
        binding.buttonSaveTraining.setOnClickListener {
            btnSaveOnClick()
        }
    }

    private fun btnSaveOnClick() {
        val date = binding.editTextDate.text.toString()
        val details = binding.editTextDetails.text.toString()

        if (date.isNotEmpty() && details.isNotEmpty()) {
            calendarViewModel.saveCalendarTraining(date, details)
            Toast.makeText(requireContext(), "Allenamento salvato", Toast.LENGTH_SHORT).show()
            checkForExistingTraining(date)
        } else {
            Toast.makeText(requireContext(), "Compila tutti i campi", Toast.LENGTH_SHORT).show()
        }
    }

    // mostra dettagli allenamento nel TextView
    private fun checkForExistingTraining(date: String) {
        calendarViewModel.calendarTrainingSessions.value?.let { sessions ->
            val training = sessions.find { it.date == date }
            if (training != null) {
                // format data italiana
                val formattedDate = try {
                    val localDate = LocalDate.parse(date)
                    String.format("%02d-%02d-%04d", localDate.dayOfMonth, localDate.monthValue, localDate.year)
                } catch (e: DateTimeParseException) {
                    date // in caso di errore, mostra la data nel formato originale
                }

                binding.textViewTrainingDetails.apply {
                    text = context.getString(R.string.allenamento_per_data, formattedDate, training.description)
                    visibility = View.VISIBLE
                }
            } else {
                binding.textViewTrainingDetails.visibility = View.GONE
            }
        }
    }
}
