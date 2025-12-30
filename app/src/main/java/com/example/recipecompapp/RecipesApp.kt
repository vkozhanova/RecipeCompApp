package com.example.recipecompapp

import androidx.compose.foundation.layout.padding
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
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme

@Composable
fun RecipesApp() {
    RecipeCompAppTheme {
        var currentScreen by remember { mutableStateOf(ScreenId.CATEGORIES) }
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = {
                        currentScreen = ScreenId.CATEGORIES
                    },
                    onFavoriteClick = {
                        currentScreen = ScreenId.FAVORITES
                    },
                )
            }
        ) { paddingValues ->
            when (currentScreen) {
                ScreenId.FAVORITES -> FavoritesScreen(
                    modifier = Modifier.padding(paddingValues)
                )

                ScreenId.CATEGORIES -> CategoriesScreen(
                    modifier = Modifier.padding(paddingValues)
                )
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