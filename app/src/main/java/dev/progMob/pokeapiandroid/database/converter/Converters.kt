package dev.progMob.pokeapiandroid.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import dev.progMob.pokeapiandroid.database.model.FavoritePokemon

class Converters {

    @TypeConverter
    fun listToJson(value: List<FavoritePokemon>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<FavoritePokemon>::class.java).toList()
}