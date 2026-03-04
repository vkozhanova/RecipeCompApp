package com.example.recipecompapp

import android.util.Log
import  androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipecompapp.ui.categories.CategoriesScreen
import com.example.recipecompapp.ui.favorites.FavoritesScreen
import com.example.recipecompapp.ui.navigation.BottomNavigation
import com.example.recipecompapp.ui.navigation.Destination
import com.example.recipecompapp.ui.recipes.RecipesScreen
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme

@Composable
fun RecipesApp() {
    RecipeCompAppTheme {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = {
                        navController.navigate(Destination.Categories.route) {
                            popUpTo(Destination.Categories.route) { inclusive = true }
                        }
                    },
                    onFavoriteClick = {
                        navController.navigate(Destination.Favorites.route) {
                            popUpTo(Destination.Favorites.route) { inclusive = true }
                        }
                    },
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Destination.Categories.route,
                modifier = Modifier.padding(paddingValues)
            ) {

                composable(Destination.Categories.route) {
                    CategoriesScreen(
                        onCategoryClick = { categoryId, _ ->
                            navController.navigate(Destination.Recipes.createRoute(categoryId))
                        }
                    )
                }

                composable(
                    route = Destination.Recipes.route,
                    arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
                    RecipesScreen(
                        categoryId = categoryId,
                        onRecipeClick = { recipeId ->
                            Log.d("DEBUG", "Клик по рецепту $recipeId")
                        }
                    )
                }

                composable(Destination.Favorites.route) {
                    FavoritesScreen()
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