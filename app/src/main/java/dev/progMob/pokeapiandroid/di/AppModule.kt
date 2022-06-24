package dev.progMob.pokeapiandroid.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.progMob.pokeapiandroid.BuildConfig
import dev.progMob.pokeapiandroidtask.api.PokemonApi
import dev.progMob.pokeapiandroid.data.repositories.DataStoreRepository
import dev.progMob.pokeapiandroid.database.PokedexDatabase
import dev.progMob.pokeapiandroid.database.dao.UserDao
import dev.progMob.pokeapiandroid.database.repository.IUserRepository
import dev.progMob.pokeapiandroid.database.repository.UserRepository
import dev.progMob.pokeapiandroid.utils.BASE_URL
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Dagger Hilt module, used in dependency injection, provides various dependencies used in the app.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext appContext: Context): DataStoreRepository {
        return DataStoreRepository(appContext.applicationContext)
    }

    @Provides
    @Singleton
    fun providePokemonApi(retrofit: Retrofit): PokemonApi = retrofit.create(PokemonApi::class.java)

    private val loggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun providesOkHttpClient(cache: Cache): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(cacheInterceptor)
            .cache(cache)
        if (BuildConfig.DEBUG) okHttpClient.addInterceptor(loggingInterceptor)

        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /*Caching data */
    @Provides
    @Singleton
    fun provideCache(@ApplicationContext appContext: Context): Cache {

        return Cache(
            File(appContext.applicationContext.cacheDir, "pokemon_cache"),
            10 * 1024 * 1024
        )
    }

    private val cacheInterceptor = object : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val response: Response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(30, TimeUnit.DAYS)
                .build()
            return response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
    }

    @Provides
    @Singleton
    fun getDB(context: Application): PokedexDatabase {
        return PokedexDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun getDao(pokeDB: PokedexDatabase): UserDao {
        return pokeDB.userDao()
    }


}