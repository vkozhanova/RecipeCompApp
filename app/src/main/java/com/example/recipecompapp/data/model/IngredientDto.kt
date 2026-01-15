package com.example.recipecompapp.data.model

import kotlinx.serialization.Serializable

@Serializable
class IngredientDto(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
)