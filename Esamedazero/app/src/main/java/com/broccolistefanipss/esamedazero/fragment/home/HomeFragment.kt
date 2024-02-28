package com.broccolistefanipss.esamedazero.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.broccolistefanipss.esamedazero.databinding.FragmentHomeBinding
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.model.User

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

        // ottiene lista di user
        val userList: List<User> = db.getData()

        // mostra i dati in TextView
        val textView: TextView = binding.homeUserName
        val displayText = buildDisplayText(userList)
        textView.text = displayText

        return root
    }

    private fun buildDisplayText(userList: List<User>): String {
        val stringBuilder = StringBuilder()

        for (user in userList) {
            stringBuilder.append("userName: ${user.userName}, Sesso: ${user.sesso}\n") // volendo append altri dati
        }
        return stringBuilder.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
