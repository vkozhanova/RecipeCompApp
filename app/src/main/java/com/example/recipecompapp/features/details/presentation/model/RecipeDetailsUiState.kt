package com.example.recipecompapp.features.details.presentation.model


import com.example.recipecompapp.ui.recipes.model.IngredientUiModel
import com.example.recipecompapp.features.recipes.presentation.model.RecipeUiModel

const val DEFAULT_SERVINGS = 2

data class RecipeDetailsUiState(
    val recipe: RecipeUiModel? = null,
    val servings: Int = DEFAULT_SERVINGS,
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val error: String? = null
) {
    val scaledIngredients: List<IngredientUiModel>
        get() {
            val stringRecipe = recipe ?: return emptyList()
        val multiplier = servings.toDouble() / DEFAULT_SERVINGS
       return stringRecipe.ingredients.map { ingredient ->
            val parts = ingredient.amount.split(' ', limit = 2)
            val number = parts.firstOrNull()?.toDoubleOrNull() ?: 0.0
            val unit = if (parts.size > 1) parts[1] else ""
            val newNumber = number * multiplier

            val newAmount = if (unit.isNotBlank()) {
                formatNumber(newNumber) + " $unit"
            } else {
                formatNumber(newNumber)
            }
            ingredient.copy(amount = newAmount)
        }
    }

    companion object {
        private fun formatNumber(value: Double): String {
            return if (value % 1.0 == 0.0) {
                value.toInt().toString()
            } else {
                "%.1f".format(value).replace(',', '.')
            }
        }
    }
}
