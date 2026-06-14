package com.example.recipecompapp.di

import androidx.lifecycle.SavedStateHandle
import com.example.recipecompapp.data.repository.RecipesRepository
import com.example.recipecompapp.features.recipes.presentation.RecipesViewModel

class RecipesViewModelFactory(
    private val saveStateHandle:  SavedStateHandle,
    private val repository: RecipesRepository
): Factory<RecipesViewModel> {
    override fun create(): RecipesViewModel {
        return RecipesViewModel(saveStateHandle, repository)
    }
}