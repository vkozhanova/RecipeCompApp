package com.example.recipecompapp.di

import com.example.recipecompapp.data.repository.RecipesRepository
import com.example.recipecompapp.data.repository.RecipesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRecipesRepository(
        repository: RecipesRepositoryImpl
    ): RecipesRepository
}