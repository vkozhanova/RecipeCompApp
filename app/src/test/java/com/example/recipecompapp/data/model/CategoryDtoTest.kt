package com.example.recipecompapp.data.model

import com.example.recipecompapp.core.Constants.IMAGES_BASE_URL
import com.example.recipecompapp.features.categories.presentation.model.toUiModel
import com.example.recipecompapp.fixtures.CategoryTestFixtures
import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryDtoTest {

    @Test
    fun `converts DTO to  UI model`() {
        val dto = CategoryTestFixtures.createCategoryDto()

        val uiModel = dto.toUiModel()

        assertEquals(dto.id, uiModel.id)
        assertEquals(dto.title, uiModel.title)
        assertEquals(dto.description, uiModel.description)
        assertEquals(IMAGES_BASE_URL + "burgers.jpg", uiModel.imageUrl)
    }

    @Test
    fun `mapper maps empty title correctly`() {
        val dto = CategoryTestFixtures.createCategoryDto(title = "")

        val uiModel = dto.toUiModel()

        assertEquals("", uiModel.title)
    }

    @Test
    fun `mapper preserves very long description`() {
        val longDescription = "A".repeat(1000)
        val dto = CategoryTestFixtures.createCategoryDto(description = longDescription)

        val uiModel = dto.toUiModel()

        assertEquals(longDescription, uiModel.description)
    }
}