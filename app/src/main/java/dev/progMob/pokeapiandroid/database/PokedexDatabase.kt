package dev.progMob.pokeapiandroid.database

import android.content.Context
import androidx.room.*
import dev.progMob.pokeapiandroid.database.converter.Converters
import dev.progMob.pokeapiandroid.database.dao.UserDao
import dev.progMob.pokeapiandroid.database.entity.UserEntity

@Database(entities = [UserEntity::class], exportSchema = false, version = 1)
@TypeConverters(Converters::class)
abstract class PokedexDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: PokedexDatabase? = null

        fun getDatabase(context: Context): PokedexDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
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