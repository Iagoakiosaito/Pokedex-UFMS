package dev.progMob.pokeapiandroid.model

import android.os.Parcelable
import dev.progMob.pokeapiandroidtask.model.PokemonResult
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonResult>
) : Parcelable