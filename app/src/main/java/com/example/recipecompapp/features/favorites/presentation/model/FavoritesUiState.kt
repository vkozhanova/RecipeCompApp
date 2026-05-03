package com.example.recipecompapp.features.favorites.presentation.model

import com.example.recipecompapp.features.recipes.presentation.model.RecipeUiModel

data class FavoritesUiState(
    val  recipes: List<RecipeUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isEmpty: Boolean get() = recipes.isEmpty() && !isLoading && error == null
}