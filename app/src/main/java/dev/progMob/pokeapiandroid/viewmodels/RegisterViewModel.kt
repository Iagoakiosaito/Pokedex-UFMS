package dev.progMob.pokeapiandroid.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.progMob.pokeapiandroid.R
import dev.progMob.pokeapiandroid.database.repository.IUserRepository
import dev.progMob.pokeapiandroid.database.repository.RegistrationParams
import dev.progMob.pokeapiandroid.database.repository.UserRepository
import dev.progMob.pokeapiandroid.utils.encrypt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: IUserRepository
    ):ViewModel() {

    val passwordError: LiveData<Int> get() = _usernameError
    private val _passwordError by lazy { MutableLiveData<Int>() }

    val usernameError: LiveData<Int> get() = _usernameError
    private val _usernameError by lazy { MutableLiveData<Int>() }

    fun register(username: String, password: String, userphoto: ByteArray, context: Context) {
        GlobalScope.launch(Dispatchers.IO) {

            val registrationParams = RegistrationParams(
                username = username,
                password = encrypt(password)!!,
                userPhoto = userphoto
            )
            userRepository.createUser(registrationParams)
        }
    }

    fun verifyUsernameAndPassword(username: String, password: String): Boolean{
        val emptyUsername = verifyEmail(username)
        val emptyPassword = password.isEmpty()

        return if(!emptyUsername && !emptyPassword){
            true
        }
        else{
            if(emptyPassword) _passwordError.value = R.string.error_empy_field
            false
        }
    }

    private fun verifyEmail(email: String): Boolean{
        return if(email.isNotEmpty()){
            false
        }else {
            _usernameError.value = R.string.error_empy_field
            true
        }

    }


}