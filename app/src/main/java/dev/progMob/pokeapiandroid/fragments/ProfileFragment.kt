package dev.progMob.pokeapiandroid.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.progMob.pokeapiandroid.R
import dev.progMob.pokeapiandroid.adapters.PokemonAdapter
import dev.progMob.pokeapiandroid.databinding.FragmentPokemonListBinding
import dev.progMob.pokeapiandroid.databinding.FragmentProfileBinding
import dev.progMob.pokeapiandroid.model.UserGlobal
import dev.progMob.pokeapiandroid.viewmodels.ProfileViewModel
import dev.progMob.pokeapiandroidtask.adapters.LoadingStateAdapter
import dev.progMob.pokeapiandroidtask.model.PokemonResult
import dev.progMob.pokeapiandroidtask.utils.PRODUCT_VIEW_TYPE
import dev.progMob.pokeapiandroidtask.utils.toggle


@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding

    private val adapter =
        PokemonAdapter { pokemonResult: PokemonResult, dominantColor: Int, picture: String? ->
            navigate(
                pokemonResult,
                dominantColor,
                picture
            )
        }

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

    private fun setAdapter() {

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == PRODUCT_VIEW_TYPE) 1
                else 2
            }
        }
        binding.pokemonList.layoutManager = gridLayoutManager
        binding.pokemonList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { retry() }
        )

        binding.pokemonList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val scrolledPosition =
                    (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()

                if (scrolledPosition != null) {
                    if (scrolledPosition >= 1) {
                        binding.scrollUp.toggle(true)
                    } else {
                        binding.scrollUp.toggle(false)
                    }
                }

            }
        })

        if (!hasInitiatedInitialCall) startFetchingPokemon(null, false); hasInitiatedInitialCall =
            true

        //the progress will only show when the adapter is refreshing and its empty
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading && adapter.snapshot().isEmpty()
            ) {
                binding.progressCircular.isVisible = true
                binding.textError.isVisible = false


            } else {
                binding.progressCircular.isVisible = false
                binding.swipeRefreshLayout.isRefreshing = false

                //if there is error a textview will show the error encountered.

                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error

                    else -> null
                }
                if (adapter.snapshot().isEmpty()) {
                    error?.let {
                        binding.textError.visibility = View.VISIBLE
                        binding.textError.setOnClickListener {
                            adapter.retry()
                        }
                    }

                }
            }
        }

    }


    private fun navigate(pokemonResult: PokemonResult, dominantColor: Int, picture: String?) {
        binding.root.findNavController()
            .navigate(
                ProfileFragmentDirections.actionProfileFragmentToPokemonStatsFragment(
                    pokemonResult,
                    dominantColor, picture
                )
            )
    }

}