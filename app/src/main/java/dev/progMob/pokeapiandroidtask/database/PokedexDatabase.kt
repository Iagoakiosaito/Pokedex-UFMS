package dev.progMob.pokeapiandroidtask.database

import android.content.Context
import androidx.room.*
import dev.progMob.pokeapiandroidtask.database.converter.Converters
import dev.progMob.pokeapiandroidtask.database.entity.User

@Database(entities = [User::class], exportSchema = false, version = 1)
@TypeConverters(Converters::class)
abstract class PokedexDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: PokedexDatabase? = null

        fun getDatabase(context: Context): PokedexDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokedexDatabase::class.java,
                    "pokedex_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}