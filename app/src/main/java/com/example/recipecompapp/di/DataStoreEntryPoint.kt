package com.example.recipecompapp.di

import com.example.recipecompapp.data.local.datastore.FavoriteDataStoreManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DataStoreEntryPoint {
    fun getFavoriteDataStoreManager(): FavoriteDataStoreManager
}