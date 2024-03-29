package com.broccolistefanipss.esamedazero.fragment.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.broccolistefanipss.esamedazero.databinding.FragmentUserBinding
import com.broccolistefanipss.esamedazero.manager.SharedPrefs

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Ottieni i dati dell'utente dalle SharedPreferences
        val userName = SharedPrefs.getString(requireContext(), SharedPrefs.userName) ?: ""
        val sex = SharedPrefs.getString(requireContext(), SharedPrefs.sesso) ?: ""
        val age = SharedPrefs.getInt(requireContext(), SharedPrefs.eta) ?: 0
        val height = SharedPrefs.getInt(requireContext(), SharedPrefs.altezza) ?: 0
        val weight = SharedPrefs.getInt(requireContext(), SharedPrefs.peso) ?: 0

        // Log dei dati recuperati
        Log.d("UserFragment", "userName: $userName")
        Log.d("UserFragment", "sesso: $sex")
        Log.d("UserFragment", "eta: $age")
        Log.d("UserFragment", "altezza: $height")
        Log.d("UserFragment", "peso: $weight")

        // Imposta i valori recuperati nei TextView del layout
        binding.UserProfile.text = userName
        binding.SexProfile.text = sex
        binding.AgeProfile.text = age.toString()
        binding.HeightProfile.text = height.toString()
        binding.WeightProfile.text = weight.toString()

        // Controllo di tutti i valori salvati nelle SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val allEntries: Map<String, *> = sharedPreferences.all

        for ((key, value) in allEntries) {
            Log.d("SharedPrefs", "$key: $value")
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
