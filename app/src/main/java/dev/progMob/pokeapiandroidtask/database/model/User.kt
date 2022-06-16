package dev.progMob.pokeapiandroidtask.database.model

import dev.progMob.pokeapiandroidtask.model.PokemonResult

data class User (
    val id: String,
    val name: String,
    val favoritePokemons: List<PokemonResult>
)
