package dev.progMob.pokeapiandroidtask.database.model

import dev.progMob.pokeapiandroidtask.model.PokemonResult

data class User (
    val id: String,
    val name: String,
    val favoritePokemons: List<PokemonResult>,
    val photo: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (name != other.name) return false
        if (favoritePokemons != other.favoritePokemons) return false
        if (!photo.contentEquals(other.photo)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + favoritePokemons.hashCode()
        result = 31 * result + photo.contentHashCode()
        return result
    }
}
