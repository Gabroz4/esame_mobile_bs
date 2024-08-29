package com.broccolistefanipss.esamedazero.fragment.user

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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.broccolistefanipss.esamedazero.activity.EditUserActivity
import com.broccolistefanipss.esamedazero.databinding.FragmentUserBinding
import com.broccolistefanipss.esamedazero.global.DB
import com.broccolistefanipss.esamedazero.manager.SessionManager
import com.broccolistefanipss.esamedazero.model.User
import java.io.OutputStream

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root


        _binding!!.btnEditProfile.setOnClickListener {
            val intent = Intent(context, EditUserActivity::class.java)
            startActivity(intent)
        }

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                imageUri = result.data?.data
                try {
                    // Gestisci l'URI e imposta l'immagine nel ImageView
                    val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.profilePicture.setImageBitmap(bitmap)
                    // Salva l'immagine nel database o nel file system
                    saveProfileImage(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Bottone per cambiare l'immagine del profilo
        binding.changeProfilePictureButton.setOnClickListener {
            openGallery()
        }

        // Carica l'immagine del profilo salvata
        loadProfileImage()


        // Ottieni i dati dell'utente dalle SharedPreferences
        //val userName = SharedPrefs.getString(requireContext(), SharedPrefs.userName) ?: ""
        val sessionManager = SessionManager(requireContext())
        val userName = sessionManager.userName ?: ""
        val database = DB(requireContext())
        val utente: User? = database.getUserData(userName)

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

        return root
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun saveProfileImage(bitmap: Bitmap) {
        try {
            // Salva l'immagine usando MediaStore senza richiedere WRITE_EXTERNAL_STORAGE
            val filename = "profile_picture_${System.currentTimeMillis()}.jpg"
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ProfilePictures")
            }

            val imageUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            imageUri?.let { uri ->
                val outputStream: OutputStream? = requireContext().contentResolver.openOutputStream(uri)
                outputStream.use { stream ->
                    if (stream != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    }
                }
                saveProfileImageUri(uri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error saving image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProfileImageUri(uri: Uri) {
        // Salva l'URI dell'immagine in SharedPreferences
        Log.d("UserFragment", "Profile image URI: $uri")
        val sharedPreferences = requireContext().getSharedPreferences("UserData", Activity.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("profile_image_uri", uri.toString())
            apply()
        }
    }

    private fun loadProfileImage() {
        val sharedPreferences = requireContext().getSharedPreferences("UserData", Activity.MODE_PRIVATE)
        val uriString = sharedPreferences.getString("profile_image_uri", null)
        uriString?.let {
            imageUri = Uri.parse(it)
            binding.profilePicture.setImageURI(imageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
