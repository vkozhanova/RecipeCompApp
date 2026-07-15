package com.example.recipecompapp.features.recipes.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.recipecompapp.data.model.IngredientDto
import com.example.recipecompapp.features.fixtures.RecipeTestFixtures
import com.example.recipecompapp.features.recipes.presentation.model.RecipesUiState
import com.example.recipecompapp.features.recipes.presentation.model.toUiModel
import org.junit.Rule
import org.junit.Test

class RecipesScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsLoadingState() {
        composeTestRule.setContent {
            RecipesContent(
                uiState = RecipesUiState(
                    recipes = emptyList(),
                    isLoading = true
                ),
                onRecipeClick = { _, _ -> }
            )
        }
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    @Test
    fun showsErrorState() {
        composeTestRule.setContent {
            RecipesContent(
                uiState = RecipesUiState(
                    recipes = emptyList(),
                    error = "Network Error"
                ),
                onRecipeClick = { _, _ -> }
            )
        }
        composeTestRule.onNodeWithTag("error_message").assertIsDisplayed()
    }

    @Test
    fun showsEmptyState() {
        composeTestRule.setContent {
            RecipesContent(
                uiState = RecipesUiState(
                    recipes = emptyList(),
                    isLoading = false
                ),
                onRecipeClick = { _, _ -> }
            )
        }
        composeTestRule.onNodeWithTag("empty_state").assertIsDisplayed()
    }

    @Test
    fun displaysRecipeList() {
        val recipeDto = RecipeTestFixtures.createRecipeDto(
            id = 1,
            title = "Чизбургер",
            ingredients = listOf(
                IngredientDto(
                    quantity = "1",
                    unitOfMeasure = "",
                    description = "Булочка"
                )
            ),
            method = listOf("Обжарить", "Посолить"),
            imageUrl = ""
        )

        val uiRecipe = recipeDto.toUiModel()

        composeTestRule.setContent {
            RecipesContent(
                uiState = RecipesUiState(
                    recipes = listOf(uiRecipe),
                    categoryTitle = "Бургеры",
                    categoryImageUrl = "",
                    isLoading = false,
                    error = null
                ),
                onRecipeClick = { _, _ -> }
            )
        }
        composeTestRule.onNodeWithText("ЧИЗБУРГЕР").assertIsDisplayed()
    }
}