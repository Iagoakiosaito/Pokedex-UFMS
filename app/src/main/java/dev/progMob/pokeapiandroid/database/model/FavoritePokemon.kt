package dev.progMob.pokeapiandroid.database.model

import dev.progMob.pokeapiandroidtask.model.PokemonResult

data class FavoritePokemon (
    val pokemonResult: PokemonResult,
    val dominant_color: Int,
    val picture: String?
    )

