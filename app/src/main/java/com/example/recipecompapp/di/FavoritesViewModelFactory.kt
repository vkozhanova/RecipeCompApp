package com.example.recipecompapp.di

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import com.example.recipecompapp.data.local.datastore.FavoriteDataStoreManager
import com.example.recipecompapp.data.repository.RecipesRepository
import com.example.recipecompapp.features.favorites.presentation.FavoritesViewModel

class FavoritesViewModelFactory(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val resources: Resources,
    private val repository: RecipesRepository,
    private val dataStoreManager: FavoriteDataStoreManager,
): Factory<FavoritesViewModel> {
    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(application, savedStateHandle, resources, repository, dataStoreManager)
    }
}
