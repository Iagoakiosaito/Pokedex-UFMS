package dev.progMob.pokeapiandroid.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import dev.progMob.pokeapiandroid.R
import dev.progMob.pokeapiandroid.databinding.FragmentRegisterBinding
import dev.progMob.pokeapiandroid.viewmodels.RegisterViewModel
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var userImageByteArray: ByteArray
    private val _viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        subscribeUI()
    }

    private fun subscribeUI() {

        _viewModel.usernameError.observe(viewLifecycleOwner) {
            showTextInputLayoutError(binding.textInputLayoutUsername, it)
        }

        _viewModel.passwordError.observe(viewLifecycleOwner) {
            showTextInputLayoutError(binding.textInputLayoutPassword, it)
        }
    }

    private fun setupUI() {

        binding.btnRegister.setOnClickListener {
            if(_viewModel.verifyUsernameAndPassword(
                    username = binding.edtUsernameRegister.text.toString(),
                    password = binding.edtPasswordRegister.text.toString()
            )){
                _viewModel.register(
                    username = binding.edtUsernameRegister.text.toString(),
                    password = binding.edtPasswordRegister.text.toString(),
                    userphoto = userImageByteArray,
                    context = requireContext()
                )
                Toast.makeText(requireContext(), R.string.register_succesfully, Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val userImageBitmap = data?.extras?.get("data") as Bitmap
                val stream  = ByteArrayOutputStream()
                userImageBitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                userImageByteArray = stream.toByteArray()
                Toast.makeText(requireContext(), R.string.photo_succesfully, Toast.LENGTH_SHORT).show()
//                val bmp = BitmapFactory.decodeByteArray(userImage, 0, userImage.size)  ByteArray to Bitmap converter
            }
        }

        binding.btnUserPhoto.setOnClickListener {
            val cInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cInt)
        }

    }

    private fun showTextInputLayoutError(editText: TextInputLayout, erro: Int){
        editText.error = getString(erro)
    }

}