package com.example.recipecompapp.data.model

import com.example.recipecompapp.data.database.entity.RecipeEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RecipeDto(
    val id: Int,
    val title: String,
    val ingredients: List<IngredientDto> = emptyList(),
    val method: List<String> = emptyList(),
    val imageUrl: String? = null,
)

fun RecipeDto.toEntity(categoryId: Int): RecipeEntity = RecipeEntity(
    id = id,
    title = title,
    categoryId = categoryId.toString(),
    imageUrl = imageUrl ?: "",
    ingredients = Json.encodeToString(ingredients),
    method = Json.encodeToString(method)
)

fun RecipeEntity.toDto(): RecipeDto = RecipeDto(
    id = id,
    title = title,
    ingredients = Json.decodeFromString(ingredients),
    method = Json.decodeFromString(method),
    imageUrl = imageUrl.ifEmpty { null }
)