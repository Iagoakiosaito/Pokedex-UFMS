package dev.progMob.pokeapiandroid.database.dao

import androidx.room.*
import dev.progMob.pokeapiandroid.database.model.FavoritePokemon
import dev.progMob.pokeapiandroid.database.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(user: UserEntity)

    @Query("SELECT * FROM user WHERE user.user_id = :userId")
    fun getUser(userId: Long): UserEntity

    @Query("SELECT * FROM user WHERE user_name = :username AND user_password = :password")
    fun login(username: String, password: String): UserEntity

    @Query("UPDATE user SET user_favorite_pokemons = :favoritePokemons WHERE user_id = :userId")
    fun UpdateUserFavoritePokemons(favoritePokemons: List<FavoritePokemon>, userId: Long)
}