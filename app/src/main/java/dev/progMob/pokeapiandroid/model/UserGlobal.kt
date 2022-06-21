package dev.progMob.pokeapiandroid.model

import dev.progMob.pokeapiandroid.database.model.FavoritePokemon
import dev.progMob.pokeapiandroidtask.model.PokemonResult

class UserGlobal {

    companion object {
        var _id: Long = 0
        var _name: String = ""
        var favoritePokemons: MutableList<FavoritePokemon>? = mutableListOf()
        var _photo: ByteArray = byteArrayOf()
    }

}