package dev.progMob.pokeapiandroid.fragments

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.progMob.pokeapiandroid.R
import dev.progMob.pokeapiandroid.adapters.ProfileFavoritePokemonsAdapter
import dev.progMob.pokeapiandroid.database.model.User
import dev.progMob.pokeapiandroid.databinding.FragmentProfileBinding
import dev.progMob.pokeapiandroid.model.UserGlobal


@AndroidEntryPoint
@SuppressLint("ClickableViewAccessibility")
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        setAdapter()
        setupUI()
    }

    private fun setupUI() {
        binding.btnExit.setOnClickListener {
            val navController = findNavController()
            navController.popBackStack()
            navController.navigate(R.id.loginFragment)
        }

        val bmp = BitmapFactory.decodeByteArray(UserGlobal._photo, 0, UserGlobal._photo.size)
        binding.imgviewUserPhoto.setImageBitmap(bmp)
        binding.txtUserNameProfile.text = UserGlobal._name.capitalize()

        val window: Window = activity!!.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity!!, R.color.green)

    }

    private fun setAdapter() {

        val favoritePokemonAdapter = ProfileFavoritePokemonsAdapter(UserGlobal.favoritePokemons!!).apply {
            onItemClick = { favoritePokemon ->
                val action = ProfileFragmentDirections
                    .actionProfileFragmentToPokemonStatsFragment(
                        picture = favoritePokemon.picture,
                        pokemonResult = favoritePokemon.pokemonResult,
                        dominantColor = favoritePokemon.dominant_color
                    )
                findNavController().navigate(action)
            }
        }

        binding.pokemonList.run {
            setHasFixedSize(true)
            if(UserGlobal.favoritePokemons!!.isNullOrEmpty()){
                binding.emptyPokemons.text = getString(R.string.no_pokemon_added)
            } else{
                adapter = favoritePokemonAdapter
            }

        }

    }

}