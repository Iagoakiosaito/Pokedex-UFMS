package dev.progMob.pokeapiandroid.fragments

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.progMob.pokeapiandroid.R
import dev.progMob.pokeapiandroid.databinding.LoginFragmentBinding
import dev.progMob.pokeapiandroid.model.UserGlobal
import dev.progMob.pokeapiandroid.viewmodels.LoginViewModel


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private val _viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        setupUI()
        subscribeUI()
    }

    private fun setupUI() {


        binding.btnLogin.setOnClickListener {
            _viewModel.login(
                username = binding.edtUsernameLogin.text.toString(),
                password = binding.edtPasswordLogin.text.toString(),
                context = requireContext()
            )
        }
        binding.btnRegister.setOnClickListener{
            val navController = findNavController()
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            navController.navigate(action)
        }
    }

    private fun subscribeUI() {
        _viewModel.messageLogin.observe(viewLifecycleOwner) {
            when(it){
                R.string.LOGIN_OK -> {
                    navigateToPokemonListFragment()
                }
                R.string.LOGIN_FAIL -> {
                    Toast.makeText(requireContext(), R.string.fail_to_login, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToPokemonListFragment() {
        UserGlobal._photo = _viewModel.userResult.value!!.photo
        UserGlobal._name = _viewModel.userResult.value!!.name
        UserGlobal.favoritePokemons = _viewModel.userResult.value!!.favoritePokemons.toMutableList()

        val navController = findNavController()
        navController.popBackStack()
        navController.navigate(R.id.pokemonListFragment)
    }


}


