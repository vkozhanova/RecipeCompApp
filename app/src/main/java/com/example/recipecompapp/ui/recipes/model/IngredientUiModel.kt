package com.example.recipecompapp.ui.recipes.model

import androidx.compose.runtime.Immutable
import com.example.recipecompapp.data.model.IngredientDto

@Immutable
data class IngredientUiModel(
    val name: String,
    val amount: String,
)

fun IngredientDto.toUiModel(): IngredientUiModel {
    val displayAmount = if (quantity.isNotEmpty() && unitOfMeasure.isNotEmpty()) {
        "$quantity $unitOfMeasure"
    } else quantity.ifEmpty { "" }

    return IngredientUiModel(
        name = this.description,
        amount = displayAmount
    )
}