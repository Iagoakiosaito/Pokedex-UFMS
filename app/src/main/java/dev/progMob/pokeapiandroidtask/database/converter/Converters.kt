package dev.progMob.pokeapiandroidtask.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import dev.progMob.pokeapiandroidtask.model.PokemonResult

class Converters {

    @TypeConverter
    fun listToJson(value: List<PokemonResult>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<PokemonResult>::class.java).toList()
}