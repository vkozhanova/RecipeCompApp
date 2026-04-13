package com.example.recipecompapp

import android.content.Intent
import android.net.Uri
import android.util.Log
import  androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipecompapp.data.repository.RecipesRepositoryStub
import com.example.recipecompapp.data.repository.RecipesRepositoryStub.getRecipeById
import com.example.recipecompapp.data.util.FavoriteDataStoreManager
import com.example.recipecompapp.ui.Constants.DEEP_LINK_BASE_URL
import com.example.recipecompapp.ui.Constants.DEEP_LINK_SCHEME
import com.example.recipecompapp.ui.categories.CategoriesScreen
import com.example.recipecompapp.ui.details.RecipeDetailsScreen
import com.example.recipecompapp.ui.favorites.FavoritesScreen
import com.example.recipecompapp.ui.navigation.BottomNavigation
import com.example.recipecompapp.ui.navigation.Destination
import com.example.recipecompapp.ui.recipes.RecipesScreen
import com.example.recipecompapp.ui.recipes.model.toUiModel
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme
import kotlinx.coroutines.delay

@Composable
fun RecipesApp(deepLinkIntent: Intent?) {
    RecipeCompAppTheme {
        val navController = rememberNavController()
        val context = LocalContext.current
        val dataStoreManager = remember { FavoriteDataStoreManager(context) }
        val favoriteCountFlow = remember { dataStoreManager.getFavoriteCountFlow() }

        LaunchedEffect(deepLinkIntent) {
            deepLinkIntent?.data?.let { uri ->
                val recipeId = parseRecipeIdFromUri(uri)
                if (recipeId != null) {
                    delay(100)
                    navController.navigate(Destination.RecipeDetails.createRoute(recipeId))
                }
            }
        }

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
                    favoriteCountFlow = favoriteCountFlow
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
                        },
                    )
                }

                composable(
                    route = Destination.Recipes.route,
                    arguments = listOf(navArgument("categoryId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
                    RecipesScreen(
                        categoryId = categoryId,
                        onRecipeClick = { recipeId, _ ->
                            Log.d("DEBUG", "Клик по рецепту $recipeId")
                            navController.navigate(Destination.RecipeDetails.createRoute(recipeId))
                        }
                    )
                }

                composable(Destination.Favorites.route) {
                    FavoritesScreen(
                        repository = RecipesRepositoryStub,
                        favoritesManager = dataStoreManager,
                        onRecipeClick = { recipeId ->
                            navController.navigate(Destination.RecipeDetails.createRoute(recipeId))
                        }
                    )
                }

                composable(
                    route = Destination.RecipeDetails.route,
                    arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: 0
                    val recipe = getRecipeById(recipeId)?.toUiModel()
                    if (recipe != null) {
                        RecipeDetailsScreen(
                            recipe = recipe,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.recipe_not_found)
                        )
                    }
                }
            }
        }
    }
}

private fun parseRecipeIdFromUri(uri: Uri): Int? {
    return when (uri.scheme) {
        DEEP_LINK_SCHEME -> {
            if (uri.host == "recipe") uri.pathSegments.firstOrNull()?.toIntOrNull()
            else null
        }

        "https", "http" -> {
            if (uri.pathSegments.firstOrNull() == "recipe") {
                uri.pathSegments.getOrNull(1)?.toIntOrNull()
            } else null
        }

        else -> null
    }
}

fun createRecipeDeepLink(recipeId: Int): String = "$DEEP_LINK_BASE_URL/recipe/$recipeId"

@Preview
@Composable
fun RecipesAppPreview() {
    RecipeCompAppTheme {
        RecipesApp(deepLinkIntent = null)
    }
}