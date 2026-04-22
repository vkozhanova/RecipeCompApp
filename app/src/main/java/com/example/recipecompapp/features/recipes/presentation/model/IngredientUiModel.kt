package com.example.recipecompapp.ui.recipes.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.example.recipecompapp.data.model.IngredientDto
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
data class IngredientUiModel(
    val name: String,
    val amount: String,
) : Parcelable

fun IngredientDto.toUiModel(): IngredientUiModel {
    val displayAmount = if (quantity.isNotEmpty() && unitOfMeasure.isNotEmpty()) {
        "$quantity $unitOfMeasure"
    } else quantity.ifEmpty { "" }

    return IngredientUiModel(
        name = this.description,
        amount = displayAmount
    )
}