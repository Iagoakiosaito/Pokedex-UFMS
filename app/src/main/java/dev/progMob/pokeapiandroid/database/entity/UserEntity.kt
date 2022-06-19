package dev.progMob.pokeapiandroidtask.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.progMob.pokeapiandroidtask.database.model.User
import dev.progMob.pokeapiandroidtask.database.repository.RegistrationParams
import dev.progMob.pokeapiandroidtask.model.PokemonResult

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    val id: Long = 0,

    @ColumnInfo(name = "user_name")
    val name: String,

    @ColumnInfo(name = "user_password")
    val hashedPassword: String,

    @ColumnInfo(name = "user_favorite_pokemons")
    val favoritePokemons: List<PokemonResult>,

    @ColumnInfo(name = "user_photo", typeAffinity = ColumnInfo.BLOB)
    val userImage: ByteArray,

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (hashedPassword != other.hashedPassword) return false
        if (favoritePokemons != other.favoritePokemons) return false
        if (!userImage.contentEquals(other.userImage)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + hashedPassword.hashCode()
        result = 31 * result + favoritePokemons.hashCode()
        result = 31 * result + userImage.contentHashCode()
        return result
    }

}

fun RegistrationParams.toUserEntity(): UserEntity {
    return with(this) {
        UserEntity(
            name = this.username,
            hashedPassword = this.password,
            favoritePokemons = mutableListOf(),
            userImage = this.userPhoto
        )
    }
}

fun UserEntity.toUser(): User {
    return User(
        id = this.id.toString(),
        name = this.name,
        favoritePokemons = this.favoritePokemons,
        photo = this.userImage
    )
}