package dev.progMob.pokeapiandroidtask.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.progMob.pokeapiandroidtask.data.repositories.PokemonRepository
import dev.progMob.pokeapiandroidtask.utils.NetworkResource
import dev.progMob.pokeapiandroidtask.utils.extractId
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class PokemonStatsViewModel @Inject constructor(private val pokemonRepository: PokemonRepository) :
    ViewModel() {

    suspend fun getSinglePokemon(url: String) = flow {
        val id = url.extractId()
        emit(NetworkResource.Loading)
        emit(pokemonRepository.getSinglePokemon(id))
    }

    fun addFavorite() {
        TODO("Not yet implemented")
    }

}