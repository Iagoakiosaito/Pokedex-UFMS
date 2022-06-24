package dev.progMob.pokeapiandroid.database.repository

import dev.progMob.pokeapiandroid.database.entity.toUser
import dev.progMob.pokeapiandroid.database.entity.toUserEntity
import dev.progMob.pokeapiandroid.database.model.FavoritePokemon
import dev.progMob.pokeapiandroid.database.model.User

interface IUserRepository {

    fun createUser(registrationParams: RegistrationParams)

    fun getUser(id: Long): User

    fun login(username: String, password: String): User

    fun updateFavoritePokemons(favoritePokemons: List<FavoritePokemon>, userId: Long)
}