package dev.progMob.pokeapiandroid.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.progMob.pokeapiandroid.R
import dev.progMob.pokeapiandroid.databinding.FragmentPokemonListBinding
import dev.progMob.pokeapiandroid.databinding.FragmentProfileBinding
import dev.progMob.pokeapiandroid.model.UserGlobal
import dev.progMob.pokeapiandroid.viewmodels.ProfileViewModel


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        val bmp = BitmapFactory.decodeByteArray(UserGlobal._photo, 0, UserGlobal._photo.size)
        binding.imgviewUserPhoto.setImageBitmap(bmp)

        setupUI()
    }

    private fun setupUI() {
        binding.btnExit.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
            navController.navigate(R.id.loginFragment)
        }
    }


}