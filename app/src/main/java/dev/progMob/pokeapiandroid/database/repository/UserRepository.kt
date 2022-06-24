package dev.progMob.pokeapiandroid.database.repository

import dev.progMob.pokeapiandroid.database.model.FavoritePokemon
import dev.progMob.pokeapiandroid.database.dao.UserDao
import dev.progMob.pokeapiandroid.database.entity.toUser
import dev.progMob.pokeapiandroid.database.entity.toUserEntity
import dev.progMob.pokeapiandroid.database.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
): IUserRepository {

    override fun createUser(registrationParams: RegistrationParams) {
        val userEntity = registrationParams.toUserEntity()
        userDao.save(userEntity)
    }

    override fun getUser(id: Long): User {
        return userDao.getUser(id).toUser()
    }

    override fun login(username: String, password: String): User {
        return userDao.login(username, password).toUser()
    }

    override fun updateFavoritePokemons(favoritePokemons: List<FavoritePokemon>, userId: Long){
        return userDao.updateUserFavoritePokemons(favoritePokemons, userId)
    }

}