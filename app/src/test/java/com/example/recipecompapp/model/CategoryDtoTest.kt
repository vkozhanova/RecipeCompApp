package com.example.recipecompapp.model

import com.example.recipecompapp.data.model.CategoryDto
import com.example.recipecompapp.features.categories.presentation.model.toUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryDtoTest {
    @Test
    fun `converts DTO to  UI model`() {
        val dto = CategoryDto(
            id = 1,
            title = "Рыба",
            description = "Блюда из рыбы",
            imageUrl = "fish.jpg"
        )

        val result = dto.toUiModel()

        assertEquals("Рыба", result.title)
        assertEquals("Блюда из рыбы", result.description)
    }
}