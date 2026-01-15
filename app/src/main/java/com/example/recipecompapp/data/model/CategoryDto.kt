package com.example.recipecompapp.data.model

import kotlinx.serialization.Serializable

@Serializable
class CategoryDto(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
)