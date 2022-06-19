package dev.progMob.pokeapiandroid.model

import dev.progMob.pokeapiandroidtask.model.PokemonResult

class UserGlobal {

    companion object {
        var _name: String = ""
        var favoritePokemons: MutableList<PokemonResult>? = mutableListOf()
        var _photo: ByteArray = byteArrayOf()
    }

}