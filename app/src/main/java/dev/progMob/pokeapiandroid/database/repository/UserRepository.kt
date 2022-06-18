package dev.progMob.pokeapiandroidtask.database.repository

import dagger.multibindings.IntKey
import dev.progMob.pokeapiandroidtask.database.dao.UserDao
import dev.progMob.pokeapiandroidtask.database.entity.toUser
import dev.progMob.pokeapiandroidtask.database.entity.toUserEntity
import dev.progMob.pokeapiandroidtask.database.model.User
import dev.progMob.pokeapiandroidtask.model.PokemonResult
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    suspend fun createUser(registrationParams: RegistrationParams) {
        val userEntity = registrationParams.toUserEntity()
        userDao.save(userEntity)
    }

    suspend fun getUser(id: Long): User {
        return userDao.getUser(id).toUser()
    }

    suspend fun login(username: String, password: String): Long {
        return userDao.login(username, password)
    }

//    fun getFavoritePokemonsFromCertainUser(id: Long): List<PokemonResult> {
//        return userDao.getFavoritePokemonsFromCertainUser(id)
//    }
}