package com.example.recipecompapp.di

import com.example.recipecompapp.data.repository.RecipesRepository
import com.example.recipecompapp.features.categories.presentation.CategoriesViewModel

class CategoriesViewModelFactory(
    private val repository: RecipesRepository
): Factory<CategoriesViewModel> {
    override fun create(): CategoriesViewModel {
        return CategoriesViewModel(repository)
    }
}