package dev.progMob.pokeapiandroid.viewmodels

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.progMob.pokeapiandroid.R
import dev.progMob.pokeapiandroid.database.repository.UserRepository
import dev.progMob.pokeapiandroidtask.database.model.User
import dev.progMob.pokeapiandroidtask.utils.encrypt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _userResult = MutableLiveData<User>()
    val userResult: LiveData<User>
        get() = _userResult

    private val _messageLogin = MutableLiveData<Int>()
    val messageLogin: LiveData<Int>
        get() = _messageLogin

    fun login(username: String, password: String, context: Context){
        GlobalScope.launch(Dispatchers.IO) {
            try {

                val encryptedPassword = encrypt(strToEncrypt = password)
                var userResult: User?
                userResult = userRepository.login(username = username, password = encryptedPassword!!)
                val a  =  userResult
                withContext(Dispatchers.Main){
                    if (userResult != null){
                        _userResult.postValue(userResult!!)
                        _messageLogin.postValue(R.string.LOGIN_OK)

                    } else {
                        _messageLogin.postValue(R.string.LOGIN_FAIL)
                    }
                }

            } catch (e: Exception){
                Log.e(TAG, "login: $e", )
                _messageLogin.postValue(R.string.LOGIN_FAIL)
            }

        }
    }
}