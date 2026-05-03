package com.example.recipecompapp.features.recipes.presentation.model

data class RecipesUiState(
    val recipes: List<RecipeUiModel> = emptyList(),
    val categoryTitle: String = "",
    val categoryImageUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isEmpty: Boolean get() = recipes.isEmpty()
}