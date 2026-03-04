package com.example.recipecompapp.ui.recipes.model

import androidx.compose.runtime.Immutable
import com.example.recipecompapp.data.model.RecipeDto
import com.example.recipecompapp.ui.Constants.ASSETS_URI_PREFIX
import kotlin.Int

@Immutable
data class RecipeUiModel(
    val id: Int,
    val title: String,
    val ingredients: List<IngredientUiModel>,
    val method: List<String>,
    val imageUrl: String?,
    val isFavorite: Boolean = false
)

fun RecipeDto.toUiModel(): RecipeUiModel {
    val processedImageUrl = when {
        this.imageUrl?.startsWith("http") == true -> {
            this.imageUrl
        }
        this.imageUrl != null -> {
            val assetUrl = ASSETS_URI_PREFIX + this.imageUrl
            assetUrl
        }
        else -> {
            null
        }
    }

    val uiIngredients = this.ingredients.map { it.toUiModel() }

    return RecipeUiModel(
        id = this.id,
        title = this.title,
        ingredients = uiIngredients,
        method = this.method,
        imageUrl = processedImageUrl,
        isFavorite = false,
    )
}