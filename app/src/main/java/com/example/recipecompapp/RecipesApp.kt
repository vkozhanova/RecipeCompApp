package com.example.recipecompapp

import  androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipecompapp.ui.categories.CategoriesScreen
import com.example.recipecompapp.ui.favorites.FavoritesScreen
import com.example.recipecompapp.ui.navigation.BottomNavigation
import com.example.recipecompapp.ui.recipes.RecipesScreen
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme

@Composable
fun RecipesApp() {
    RecipeCompAppTheme {
        var currentScreen by remember { mutableStateOf(ScreenId.CATEGORIES) }
        var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
        var selectedCategoryTitle by remember { mutableStateOf<String?>(null) }

        Scaffold(
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = {
                        currentScreen = ScreenId.CATEGORIES
                        selectedCategoryId = null
                        selectedCategoryTitle = null
                    },
                    onFavoriteClick = {
                        currentScreen = ScreenId.FAVORITES
                    },
                )
            }
        ) { paddingValues ->
            when (currentScreen) {
                ScreenId.FAVORITES -> {
                FavoritesScreen(
                    modifier = Modifier.padding(paddingValues)
                )
            }

                ScreenId.CATEGORIES -> {
                    CategoriesScreen(
                        modifier = Modifier.padding(paddingValues),
                        onCategoryClick = { categoryId, categoryTitle ->
                            println("DEBUG RecipesApp: Клик по категории - id=$categoryId, title=$categoryTitle")
                            selectedCategoryId = categoryId
                            selectedCategoryTitle = categoryTitle
                            currentScreen = ScreenId.RECIPES
                        }
                    )
                }

                ScreenId.RECIPES -> {
                    val categoryId = selectedCategoryId ?: run {
                        currentScreen = ScreenId.CATEGORIES
                        return@Scaffold
                    }

                    val categoryTitle = selectedCategoryTitle ?: run {
                        currentScreen = ScreenId.CATEGORIES
                        return@Scaffold
                    }

                    RecipesScreen(
                        categoryId = categoryId,
                        categoryTitle = categoryTitle,
                        onRecipeClick = { recipeId ->
                        },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}




    @Preview
    @Composable
    fun RecipesAppPreview() {
        RecipeCompAppTheme {
            RecipesApp()
        }
    }