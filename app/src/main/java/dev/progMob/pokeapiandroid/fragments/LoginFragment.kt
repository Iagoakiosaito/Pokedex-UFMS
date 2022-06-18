package dev.progMob.pokeapiandroidtask.fragments

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.progMob.pokeapiandroidtask.utils.decrypt
import dev.progMob.pokeapiandroid.databinding.LoginFragmentBinding
import dev.progMob.pokeapiandroidtask.viewmodels.LoginViewModel
import dev.progMob.pokeapiandroidtask.utils.encrypt

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        setupUI()
    }

    private fun setupUI() {
        binding.btnLogin.setOnClickListener {
            val teste = encrypt(requireContext(), binding.edtUsernameLogin.text.toString())
            val a = teste.toString()

            decrypt(requireContext(), a.toByteArray())
        }
        binding.btnRegister.setOnClickListener{
            val navController = findNavController()
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            navController.navigate(action)
        }
    }

}