package dev.progMob.pokeapiandroid.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import dev.progMob.pokeapiandroid.R
import dev.progMob.pokeapiandroid.database.model.FavoritePokemon
import dev.progMob.pokeapiandroid.databinding.FragmentPokemonStatsBinding
import dev.progMob.pokeapiandroid.model.UserGlobal
import dev.progMob.pokeapiandroidtask.adapters.StatsAdapter
import dev.progMob.pokeapiandroid.model.PokemonResult
import dev.progMob.pokeapiandroid.model.Stats
import dev.progMob.pokeapiandroid.utils.NetworkResource
import dev.progMob.pokeapiandroid.utils.toast
import dev.progMob.pokeapiandroid.viewmodels.PokemonStatsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PokemonStatsFragment : Fragment(R.layout.fragment_pokemon_stats) {

    private lateinit var binding: FragmentPokemonStatsBinding
    private val adapter = StatsAdapter()
    private val args = PokemonStatsFragmentArgs
    private val _viewModel: PokemonStatsViewModel by viewModels()
    private lateinit var toolbar: Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPokemonStatsBinding.bind(view)
        val argument = arguments?.let { args.fromBundle(it) }
        val pokemonResult = argument?.pokemonResult
        val dominantColor = argument?.dominantColor
        val picture = argument?.picture
        toolbar = activity!!.findViewById(R.id.main_toolbar)
        toolbar.setBackgroundColor(argument?.dominantColor!!)
        val verify = FavoritePokemon(
            pokemonResult = argument?.pokemonResult,
            dominant_color = argument?.dominantColor,
            picture = argument?.picture
        )
        if(UserGlobal.favoritePokemons?.contains(verify) == true){
            binding.favoriteButton.isChecked = true
        }

        //setting the colors based on dominant colors
        if (dominantColor != 0) {
            dominantColor?.let { theColor ->
                binding.card.setBackgroundColor(theColor)
                requireActivity().window.statusBarColor = theColor
            }
        }

        setupUI()
        val toolbar = activity?.findViewById<Toolbar>(R.id.main_toolbar)
        toolbar?.title = pokemonResult?.name?.capitalize()


        //load pic
        binding.apply {
            Glide.with(root)
                .load(picture)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(pokemonItemImage)
        }

        pokemonResult?.let { loadSinglePokemon(it) }

    }

    private fun setupUI() {

        //favorite button selected
        binding.favoriteButton.setOnCheckedChangeListener { _, isChecked ->
            val argument = arguments?.let { args.fromBundle(it) }
            val favoritePokemon = FavoritePokemon(
                pokemonResult = argument?.pokemonResult!!,
                dominant_color = argument?.dominantColor,
                picture = argument?.picture
            )
            if(isChecked){
                _viewModel.addFavorite(favoritePokemon)
            } else {
                _viewModel.removeFavorite(favoritePokemon)
            }
        }
    }

    private fun loadSinglePokemon(pokemonResult: PokemonResult) {

        lifecycleScope.launch(Dispatchers.Main) {
            //a bit delay for the animation to finish
            delay(300)
            _viewModel.getSinglePokemon(pokemonResult.url).collect {
                when (it) {
                    is NetworkResource.Success -> {
                        binding.progressCircular.isVisible = false
                        binding.apply {
                            (it.value.weight.div(10.0).toString() + " kgs").also { weight ->
                                pokemonItemWeight.text = weight
                            }
                            (it.value.height.div(10.0).toString() + " meters").also { height ->
                                pokemonItemHeight.text = height
                            }
                            pokemonStatList.adapter = adapter
                            adapter.setStats(it.value.stats as ArrayList<Stats>)
                        }
                    }
                    is NetworkResource.Failure -> {
                        binding.progressCircular.isVisible = false
                        requireContext().toast("There was an error loading the pokemon")
                    }
                    is NetworkResource.Loading -> {
                        binding.progressCircular.isVisible = true
                    }
                }
            }
        }
    }
}