package com.example.recipecompapp.data.model

import com.example.recipecompapp.core.Constants
import com.example.recipecompapp.features.recipes.presentation.model.toUiModel
import com.example.recipecompapp.fixtures.RecipeTestFixtures
import com.example.recipecompapp.ui.recipes.model.toUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class RecipeDtoMapperTest {
    private lateinit var baseUrl: String

    @Before
    fun setUp() {
        baseUrl = Constants.IMAGES_BASE_URL
    }

    @Test
    fun `maps DTO to UI model correctly`() {
        val dto = RecipeTestFixtures.createRecipeDto()

        val uiModel = dto.toUiModel()

        assertEquals(dto.id, uiModel.id)
        assertEquals(dto.title, uiModel.title)
        assertEquals(dto.method, uiModel.method)
        assertEquals(dto.ingredients.map { it.toUiModel() }, uiModel.ingredients)
        assertFalse(uiModel.isFavorite)
    }

    @Test
    fun `prepends base url to relative imageUrl`() {
        val path = "pasta.jpg"
        val dto = RecipeTestFixtures.createRecipeDto(imageUrl = path)

        val uiModel = dto.toUiModel()
        val expectedUrl = baseUrl + path
        assertEquals(expectedUrl, uiModel.imageUrl)
    }

    @Test
    fun `preserves full imageUrl starting with http`() {
        val fullUrl = "https://recipes.androidsprint.ru/api/images/pasta.jpg"
        val dto = RecipeTestFixtures.createRecipeDto(imageUrl = fullUrl)

        val uiModel = dto.toUiModel()

        assertEquals(fullUrl, uiModel.imageUrl)
    }
}