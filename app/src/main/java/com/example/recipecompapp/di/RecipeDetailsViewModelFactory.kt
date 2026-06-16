package com.example.recipecompapp.di

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import com.example.recipecompapp.data.local.datastore.FavoriteDataStoreManager
import com.example.recipecompapp.data.repository.RecipesRepository
import com.example.recipecompapp.features.details.presentation.RecipeDetailsViewModel

class RecipeDetailsViewModelFactory(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val resources: Resources,
    private val repository: RecipesRepository,
    private val dataStoreManager: FavoriteDataStoreManager
): Factory<RecipeDetailsViewModel> {
    override fun create(): RecipeDetailsViewModel {
        return RecipeDetailsViewModel(application, savedStateHandle, resources, repository, dataStoreManager)
    }
}