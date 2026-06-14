package com.example.recipecompapp.di

import android.content.Context
import com.example.recipecompapp.BuildConfig
import com.example.recipecompapp.data.database.RecipesDatabase
import com.example.recipecompapp.data.local.datastore.FavoriteDataStoreManager
import com.example.recipecompapp.data.network.NetworkConfig.BASE_URL
import com.example.recipecompapp.data.network.api.RecipesApiService
import com.example.recipecompapp.data.repository.RecipesRepository
import com.example.recipecompapp.data.repository.RecipesRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class AppContainer(context: Context) {

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
    }
    val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()


    val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()

    val apiService = retrofit.create(RecipesApiService::class.java)
    val database = RecipesDatabase.buildDatabase(context)
    val repository: RecipesRepository = RecipesRepositoryImpl(apiService, database)
    val favoriteDataStoreManager: FavoriteDataStoreManager by lazy {
        FavoriteDataStoreManager(context)
    }
}