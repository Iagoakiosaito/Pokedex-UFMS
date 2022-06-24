package dev.progMob.pokeapiandroid.database.repository

data class RegistrationParams (

    var username: String = "",
    var password: String = "",
    var userPhoto: ByteArray = byteArrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegistrationParams

        if (username != other.username) return false
        if (password != other.password) return false
        if (!userPhoto.contentEquals(other.userPhoto)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + userPhoto.contentHashCode()
        return result
    }

}
