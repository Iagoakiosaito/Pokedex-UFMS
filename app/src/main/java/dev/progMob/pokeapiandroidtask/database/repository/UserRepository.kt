package dev.progMob.pokeapiandroidtask.database.repository

import dev.progMob.pokeapiandroidtask.database.model.User

interface UserRepository {

    fun createUser(registrationParams: RegistrationParams)

    fun getUser(id: Long): User

    fun login(username: String, password: String): Long

}