package dev.progMob.pokeapiandroidtask.model

import android.os.Parcelable
import dev.progMob.pokeapiandroidtask.model.Stat
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stats(
    val base_stat: Int,
    val effort: Int,
    val stat: Stat
) : Parcelable