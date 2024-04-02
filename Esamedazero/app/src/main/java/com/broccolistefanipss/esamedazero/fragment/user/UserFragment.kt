package com.broccolistefanipss.esamedazero.fragment.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.broccolistefanipss.esamedazero.databinding.FragmentUserBinding
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager
import com.broccolistefanipss.esamedazero.manager.SharedPrefs
import com.broccolistefanipss.esamedazero.model.User

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
        //val userName = SharedPrefs.getString(requireContext(), SharedPrefs.userName) ?: ""
        val sessionManager = SessionManager(requireContext())
        val userName = sessionManager.userName ?: ""
        val database = DB(requireContext())
        val utente: User? = database.getUserData(userName)

        //val sex = utente?.let { SharedPrefs.getString(requireContext(), it.sesso) } ?: ""
        //val age = utente?.let { SharedPrefs.getInt(requireContext(), it.eta) } ?: 0
        //val height = utente?.let { SharedPrefs.getInt(requireContext(), it.altezza) } ?: 0
        //val weight = utente?.let { SharedPrefs.getDouble(requireContext(), it.peso) } ?: 0
        val sex = utente?.sesso ?: ""
        val age = utente?.eta ?: 0
        val height = utente?.altezza ?: 0
        val weight = utente?.peso ?: 0
        val objective = utente?.obiettivo ?: 0



        // Log dei dati recuperati
        Log.d("UserFragment", "userName: $userName")
        Log.d("UserFragment", "sesso: $sex")
        Log.d("UserFragment", "eta: $age")
        Log.d("UserFragment", "altezza: $height")
        Log.d("UserFragment", "peso: $weight")
        Log.d("UserFragment", "peso: $objective")

        // Imposta i valori recuperati nei TextView del layout
        binding.UserProfile.text = userName
        binding.SexProfile.text = sex
        binding.AgeProfile.text = age.toString()
        binding.HeightProfile.text = height.toString()
        binding.WeightProfile.text = weight.toString()
        binding.objectiveProfile.text = objective.toString()

        // Controllo di tutti i valori salvati nelle SharedPreferences
        //val sharedPreferences = requireContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        //val allEntries: Map<String, *> = sharedPreferences.all

        //for ((key, value) in allEntries) {
        //    Log.d("SharedPrefs", "$key: $value")
        //}

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
