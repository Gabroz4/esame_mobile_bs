package com.broccolistefanipss.sportstracker.fragment.calendar

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.format.DateTimeFormatter

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarViewModel: CalendarViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        calendarViewModel = ViewModelProvider(this)[CalendarViewModel::class.java]

        val db = DB(requireContext())
        calendarViewModel.init(db, requireContext())  // Passa il context al viewModel

        sharedPreferences =
            requireContext().getSharedPreferences("TrainingPrefs", Context.MODE_PRIVATE)

        setupCalendarClickListener()
        setupSaveButtonListener()
        observeCalendarTrainings()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Osserva il LiveData dal ViewModel per decorare le date con allenamenti programmati
    private fun observeCalendarTrainings() {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")  // Definisci il formatter

        calendarViewModel.calendarTrainingSessions.observe(viewLifecycleOwner) { sessions ->
            val calendarDays = sessions.mapNotNull { session ->
                try {
                    val localDate = LocalDate.parse(session.date, formatter)
                    Log.d("CalendarFragment", "Parsed date: $localDate")
                    CalendarDay.from(localDate.year, localDate.monthValue, localDate.dayOfMonth)
                } catch (e: DateTimeParseException) {
                    Log.e("CalendarFragment", "Error parsing date: ${session.date}")
                    null
                }
            }.toSet()

            Log.d("CalendarFragment", "Dates to decorate: $calendarDays")

            // Assicurati di rimuovere i decoratori prima di aggiungerne di nuovi
            //binding.calendarView.removeDecorators()

            //if (calendarDays.isNotEmpty()) {
            //    binding.calendarView.addDecorator(EventDecorator(requireContext(), calendarDays))
//
            //    // Forza l'aggiornamento dei decoratori
            //    binding.calendarView.invalidateDecorators()
            //}
        }

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
            val date = binding.editTextDate.text.toString()
            val details = binding.editTextDetails.text.toString()

            if (date.isNotEmpty() && details.isNotEmpty()) {
                calendarViewModel.saveCalendarTraining(date, details)  // Save via ViewModel
                Toast.makeText(requireContext(), "Allenamento salvato", Toast.LENGTH_SHORT).show()
                checkForExistingTraining(date)
            } else {
                Toast.makeText(requireContext(), "Compila tutti i campi", Toast.LENGTH_SHORT).show()
            }
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
