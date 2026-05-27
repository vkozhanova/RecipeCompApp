package com.example.recipecompapp.features.categories.presentation.model

import androidx.compose.runtime.Immutable
import com.example.recipecompapp.data.model.CategoryDto
import com.example.recipecompapp.core.Constants.IMAGES_BASE_URL
import kotlin.String

@Immutable
data class CategoryUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
)

fun CategoryDto.toUiModel(): CategoryUiModel {
    val processedImageUrl = if (this.imageUrl?.startsWith("http") == true) {
        this.imageUrl
    } else {
        IMAGES_BASE_URL + (this.imageUrl ?: "")
    }
    return CategoryUiModel(
        id = this.id,
        title = this.title,
        description = this.description,
        imageUrl = processedImageUrl
    )
}