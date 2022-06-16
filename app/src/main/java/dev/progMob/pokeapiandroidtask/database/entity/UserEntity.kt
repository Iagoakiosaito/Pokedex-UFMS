package dev.progMob.pokeapiandroidtask.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.progMob.pokeapiandroidtask.model.PokemonResult

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val id: Long = 0,

    @ColumnInfo(name = "user_name")
    val name: String,

    @ColumnInfo(name = "user_password")
    val password: String,

    @ColumnInfo(name = "user_favorite_pokemons")
    val favoritePokemons: List<PokemonResult>
)
