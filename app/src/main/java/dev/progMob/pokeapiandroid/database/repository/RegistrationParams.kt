package dev.progMob.pokeapiandroidtask.database.repository

import dev.progMob.pokeapiandroidtask.model.PokemonResult

data class RegistrationParams (

    var username: String = "",
    var password: ByteArray = byteArrayOf(),
    var userPhoto: ByteArray = byteArrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegistrationParams

        if (username != other.username) return false
        if (!password.contentEquals(other.password)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + password.contentHashCode()
        return result
    }
}
