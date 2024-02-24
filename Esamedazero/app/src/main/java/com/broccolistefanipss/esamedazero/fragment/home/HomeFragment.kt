package com.broccolistefanipss.esamedazero.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.broccolistefanipss.esamedazero.databinding.FragmentHomeBinding
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.model.Utente

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: DB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db = DB(requireContext())

        // Assuming you have a method in DB to fetch data
        val utenteList: List<Utente> = db.getData()

        // Display the data in a TextView (Modify based on your UI layout)
        val textView: TextView = binding.userName
        val displayText = buildDisplayText(utenteList)
        textView.text = displayText

        return root
    }

    private fun buildDisplayText(utenteList: List<Utente>): String {
        val stringBuilder = StringBuilder()

        for (utente in utenteList) {
            stringBuilder.append("userName: ${utente.userName}, Sesso: ${utente.sesso}\n")
            // Append other relevant data

        }
        return stringBuilder.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
