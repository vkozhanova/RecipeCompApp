package com.example.recipecompapp.features.categories.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.recipecompapp.features.categories.presentation.model.CategoriesUiState
import com.example.recipecompapp.features.categories.presentation.model.CategoryUiModel
import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test

class CategoriesScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysCategories() {
        composeTestRule.setContent {
            CategoriesContent(
                uiState = CategoriesUiState(
                    categories = listOf(CategoryUiModel(1, "Бургеры", "", ""))
                ),
                onCategoryClick = { _, _, _ -> }
            )
        }
        composeTestRule.onNodeWithText("БУРГЕРЫ").assertIsDisplayed()
    }

    @Test
    fun clickingCategoryNavigatesToRecipes() {
        var clickedId = 0
        composeTestRule.setContent {
            CategoriesContent(
                uiState = CategoriesUiState(
                    categories = listOf(CategoryUiModel(1, "Бургеры", "", ""))
                ),
                onCategoryClick = { id, _, _ -> clickedId = id }
            )
        }
        composeTestRule.onNodeWithText("БУРГЕРЫ").performClick()
        assertEquals(1, clickedId)
    }

    @Test
    fun showsLoadingState() {
        composeTestRule.setContent {
            CategoriesContent(
                uiState = CategoriesUiState(
                    categories = emptyList(),
                    isLoading = true
                ),
                onCategoryClick = { _, _, _ -> }
            )
        }
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }
}