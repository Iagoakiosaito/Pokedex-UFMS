package dev.progMob.pokeapiandroid.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.progMob.pokeapiandroid.database.model.FavoritePokemon
import dev.progMob.pokeapiandroid.database.repository.UserRepository
import dev.progMob.pokeapiandroid.model.UserGlobal
import dev.progMob.pokeapiandroidtask.data.repositories.PokemonRepository
import dev.progMob.pokeapiandroidtask.database.repository.RegistrationParams
import dev.progMob.pokeapiandroidtask.model.PokemonResult
import dev.progMob.pokeapiandroidtask.utils.NetworkResource
import dev.progMob.pokeapiandroidtask.utils.extractId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonStatsViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository,
    private val userRepository: UserRepository
) :
    ViewModel() {

    suspend fun getSinglePokemon(url: String) = flow {
        val id = url.extractId()
        emit(NetworkResource.Loading)
        emit(pokemonRepository.getSinglePokemon(id))
    }

    fun addFavorite(favoritePokemon: FavoritePokemon) {

        UserGlobal.favoritePokemons?.add(favoritePokemon)
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateFavoritePokemons(UserGlobal.favoritePokemons!!, UserGlobal._id)
        }

    }

    fun removeFavorite(favoritePokemon: FavoritePokemon) {

        UserGlobal.favoritePokemons?.remove(favoritePokemon)
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateFavoritePokemons(UserGlobal.favoritePokemons!!, UserGlobal._id)
        }

    }

}