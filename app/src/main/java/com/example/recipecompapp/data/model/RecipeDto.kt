package com.example.recipecompapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    val id: Int,
    val title: String,
    val ingredients: List<IngredientDto> = emptyList(),
    val method: List<String> = emptyList(),
    val imageUrl: String? = null,
)