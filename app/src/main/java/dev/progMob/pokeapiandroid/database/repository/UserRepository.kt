package dev.progMob.pokeapiandroid.database.repository

import dev.progMob.pokeapiandroid.database.model.FavoritePokemon
import dev.progMob.pokeapiandroidtask.database.dao.UserDao
import dev.progMob.pokeapiandroidtask.database.entity.toUser
import dev.progMob.pokeapiandroidtask.database.entity.toUserEntity
import dev.progMob.pokeapiandroidtask.database.model.User
import dev.progMob.pokeapiandroidtask.database.repository.RegistrationParams
import dev.progMob.pokeapiandroidtask.model.PokemonResult
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    fun createUser(registrationParams: RegistrationParams) {
        val userEntity = registrationParams.toUserEntity()
        userDao.save(userEntity)
    }

    fun getUser(id: Long): User {
        return userDao.getUser(id).toUser()
    }

    fun login(username: String, password: String): User {
        return userDao.login(username, password).toUser()
    }

    fun updateFavoritePokemons(favoritePokemons: List<FavoritePokemon>, userId: Long){
        return userDao.UpdateUserFavoritePokemons(favoritePokemons, userId)
    }

//    fun getFavoritePokemonsFromCertainUser(id: Long): List<PokemonResult> {
//        return userDao.getFavoritePokemonsFromCertainUser(id)
//    }
}