package com.example.recipecompapp.data.repository

import com.example.recipecompapp.di.RepositoryModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object TestRepositoryModule {
    @Provides
    fun provideRecipesRepository(): RecipesRepository =
        StubRecipesRepository()
}