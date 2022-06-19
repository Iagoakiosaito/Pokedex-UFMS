package dev.progMob.pokeapiandroidtask.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.progMob.pokeapiandroidtask.database.entity.UserEntity
import dev.progMob.pokeapiandroidtask.model.PokemonResult

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(user: UserEntity)

    @Query("SELECT * FROM user WHERE user.user_id = :userId")
    fun getUser(userId: Long): UserEntity

//    @Query("SELECT user_favorite_pokemons FROM user WHERE user.user_id = :userId")
//    fun getFavoritePokemonsFromCertainUser(userId: Long): List<PokemonResult>

    @Query("SELECT * FROM user WHERE user_name = :username AND user_password = :password")
    fun login(username: String, password: String): UserEntity
}