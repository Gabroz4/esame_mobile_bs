package com.broccolistefanipss.sportstracker.fragment.user

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.broccolistefanipss.sportstracker.R
import com.broccolistefanipss.sportstracker.activity.EditUserActivity
import com.broccolistefanipss.sportstracker.databinding.FragmentUserBinding
import com.broccolistefanipss.sportstracker.global.DB
import com.broccolistefanipss.sportstracker.manager.LoginManager
import com.broccolistefanipss.sportstracker.manager.SessionManager
import com.broccolistefanipss.sportstracker.model.User

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var editProfileLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private lateinit var sessionManager: SessionManager
    private lateinit var database: DB
    private lateinit var loginManager: LoginManager
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loginManager = LoginManager(requireContext())
        sessionManager = SessionManager(requireContext())
        database = DB(requireContext())

        setupEditProfileLauncher()
        setupImagePicker()
        setupEditProfileButton()
        setupChangeProfilePictureButton()

        userViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.UserProfile.text = it.userName
                binding.SexProfile.text = getString(R.string.sesso_view, it.sesso)
                binding.AgeProfile.text = getString(R.string.eta_view, it.eta)
                binding.HeightProfile.text = getString(R.string.altezza_view, it.altezza)
                binding.WeightProfile.text = getString(R.string.peso_view, it.peso)
                binding.objectiveProfile.text = getString(R.string.obiettivo_view, it.obiettivo)
            }
        }
        disconnect()
        return root
    }

    override fun onResume() {
        super.onResume()
        Log.d("UserFragment", "Caricamento dei dati utente")
        userViewModel.loadUserData(DB(requireContext()), SessionManager(requireContext()))
        loadProfileImage() // carica l'immagine del profilo per l'utente corrente
    }

    private fun setupEditProfileLauncher() {
        editProfileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) { // chiede risultato a edituseractivity
                Log.d("UserFragment", "Modifiche ricevute correttamente")
                loadUserData() //se tutto ok ricarica i dati utente
            } else {
                Log.d("UserFragment", "Modifica fallita")
            }
        }
    }

    private fun setupImagePicker() {  // setup selezione immagine dalla galleria
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                imageUri = result.data?.data //se immagine esiste da all'uri il suo nome
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(imageUri!!) //apre input stream
                    val bitmap = BitmapFactory.decodeStream(inputStream) //traduce stream in bitmap
                    binding.profilePicture.setImageBitmap(bitmap) //imposta l'immagine
                    saveProfileImage(bitmap) //salva e associa all'utente con sessionmanager
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Errore nel caricamento dell'immagine", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupEditProfileButton() {
        binding.btnEditProfile.setOnClickListener {
            editProfileIntentClick()
        }
    }

    private fun editProfileIntentClick() {
        editProfileLauncher.launch(Intent(requireContext(), EditUserActivity::class.java))
    }

    private fun setupChangeProfilePictureButton() {
        binding.changeProfilePictureButton.setOnClickListener {
            openGallery()
        }
    }

    private fun loadUserData() {
        val userName = sessionManager.userName ?: ""
        Log.d("UserFragment", "Caricamento dati di: $userName")
        val utente: User? = database.getUserData(userName)

        if (utente != null) {
            binding.UserProfile.text = utente.userName
            binding.SexProfile.text = utente.sesso
            binding.AgeProfile.text = utente.eta.toString()
            binding.HeightProfile.text = utente.altezza.toString()
            binding.WeightProfile.text = utente.peso.toString()
            binding.objectiveProfile.text = utente.obiettivo
        } else {
            Toast.makeText(requireContext(), "Username non trovato", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun saveProfileImage(bitmap: Bitmap) {
        try {
            val userName = sessionManager.userName ?: return
            val filename = "profile_picture_${System.currentTimeMillis()}.jpg" //nome di base univoco dell'immagine
            val contentValues = ContentValues().apply { //prepara dati dell'immagine
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)  //nome
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // formato
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ProfilePictures") //path
            }

            //inserisce riga con contentvalues nel db di sistema MediaStore.Images.Media
            val imageUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            imageUri?.let { uri ->
                requireContext().contentResolver.openOutputStream(uri)?.use { stream -> // apre stream di output
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream) //riconverte bitmap in jpeg
                }
                sessionManager.saveProfileImageUri(uri, userName) //associa all'utente
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Errore nel salvataggio dell'immagine", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadProfileImage() {
        val userName = sessionManager.userName ?: return
        imageUri = sessionManager.getProfileImageUri(userName)

        imageUri?.let {
            binding.profilePicture.setImageURI(it)
        }
    }

    private fun disconnect() {
        val btnDisconnect: TextView = binding.btnDisconnect
        btnDisconnect.setOnClickListener {
            loginManager.logout()  // logout dell'utente
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}