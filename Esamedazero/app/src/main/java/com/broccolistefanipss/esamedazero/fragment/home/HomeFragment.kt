package com.broccolistefanipss.esamedazero.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.broccolistefanipss.esamedazero.databinding.FragmentHomeBinding
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.model.User

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(DB(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.users.observe(viewLifecycleOwner) { userList ->
            // mostra i dati in TextView
            val displayText = buildDisplayText(userList)
            binding.homeUserName.text = displayText
        }
        return root
    }

    private fun buildDisplayText(userList: List<User>): String {
        return userList.joinToString(separator = "\n") { user ->
            "userName: ${user.userName}, Sesso: ${user.sesso}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}