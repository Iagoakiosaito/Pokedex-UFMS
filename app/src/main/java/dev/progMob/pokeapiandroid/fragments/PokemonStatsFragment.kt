package dev.progMob.pokeapiandroid.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import dev.progMob.pokeapiandroid.R
import dev.progMob.pokeapiandroid.databinding.FragmentPokemonStatsBinding
import dev.progMob.pokeapiandroidtask.adapters.StatsAdapter
import dev.progMob.pokeapiandroidtask.model.PokemonResult
import dev.progMob.pokeapiandroidtask.model.Stats
import dev.progMob.pokeapiandroidtask.utils.NetworkResource
import dev.progMob.pokeapiandroidtask.utils.toast
import dev.progMob.pokeapiandroidtask.viewmodels.PokemonStatsViewModel
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
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPokemonStatsBinding.bind(view)
        val argument = arguments?.let { args.fromBundle(it) }
        val pokemonResult = argument?.pokemonResult
        val dominantColor = argument?.dominantColor
        val picture = argument?.picture
        toolbar = activity!!.findViewById(R.id.main_toolbar)
        toolbar.setBackgroundColor(argument?.dominantColor!!)

        //setting the colors based on dominant colors
        if (dominantColor != 0) {
            dominantColor?.let { theColor ->
                binding.card.setBackgroundColor(theColor)
                binding.toolbar.setBackgroundColor(theColor)
                requireActivity().window.statusBarColor = theColor
            }
        }

        setupUI()

        val toolbar = binding.toolbar
        toolbar.elevation = 0.0F
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.title = pokemonResult?.name?.capitalize()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)

        toolbar.setNavigationOnClickListener {
            binding.root.findNavController().navigateUp()
        }

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
            if(isChecked){
                _viewModel.addFavorite()
            } else {
//                binding.favoriteButton.setImageResource(R.drawable.ic_favorited)
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
                            (it.value.height.div(10.0).toString() + " metros").also { height ->
                                pokemonItemHeight.text = height
                            }
                            pokemonStatList.adapter = adapter
                            adapter.setStats(it.value.stats as ArrayList<Stats>)
                        }
                    }
                    is NetworkResource.Failure -> {
                        binding.progressCircular.isVisible = false
                        requireContext().toast("Aconteceu um erro durante o carregamento do Pokémon")
                    }
                    is NetworkResource.Loading -> {
                        binding.progressCircular.isVisible = true
                    }
                }
            }
        }
    }
}