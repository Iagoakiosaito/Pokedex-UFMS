package dev.progMob.pokeapiandroidtask.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.progMob.pokeapiandroidtask.database.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(user: User)

    @Query("SELECT * FROM user WHERE user.user_id = :userId")
    fun getUser(userId: Long)

    @Query("SELECT user_favorite_pokemons FROM user WHERE user.user_id = :userId")
    fun getFavoritePokemonsFromCertainUser(userId: Long)
}