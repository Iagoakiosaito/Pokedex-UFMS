package dev.progMob.pokeapiandroid.fragments

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.progMob.pokeapiandroid.R
import dev.progMob.pokeapiandroid.adapters.PokemonAdapter
import dev.progMob.pokeapiandroid.adapters.ProfileFavoritePokemonsAdapter
import dev.progMob.pokeapiandroid.databinding.FragmentPokemonListBinding
import dev.progMob.pokeapiandroid.databinding.FragmentProfileBinding
import dev.progMob.pokeapiandroid.model.UserGlobal
import dev.progMob.pokeapiandroid.model.UserGlobal.Companion.favoritePokemons
import dev.progMob.pokeapiandroid.viewmodels.ProfileViewModel
import dev.progMob.pokeapiandroidtask.adapters.LoadingStateAdapter
import dev.progMob.pokeapiandroidtask.model.PokemonResult
import dev.progMob.pokeapiandroidtask.utils.PRODUCT_VIEW_TYPE
import dev.progMob.pokeapiandroidtask.utils.toggle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
@SuppressLint("ClickableViewAccessibility")
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()
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
        binding.txtUserNameProfile.text = UserGlobal._name
    }

    private fun startFetchingPokemon(searchString: String?, shouldSubmitEmpty: Boolean) {

    }

    private fun setAdapter() {

        val favoritePokemonAdapter = ProfileFavoritePokemonsAdapter(UserGlobal.favoritePokemons!!).apply {
            onItemClick = { favoritePokemon ->
                val action = ProfileFragmentDirections
                    .actionProfileFragmentToPokemonStatsFragment(
                        picture = favoritePokemon.picture,
                        pokemonResult = favoritePokemon.pokemonResult
                    )
                findNavController().navigate(action)
            }
        }

        binding.pokemonList.run {
            setHasFixedSize(true)
            adapter = favoritePokemonAdapter
        }

    }

}