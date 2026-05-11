package com.example.recipecompapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import  androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipecompapp.core.Constants
import com.example.recipecompapp.data.local.datastore.FavoriteDataStoreManager
import com.example.recipecompapp.core.Constants.DEEP_LINK_BASE_URL
import com.example.recipecompapp.core.Constants.DEEP_LINK_SCHEME
import com.example.recipecompapp.features.categories.ui.CategoriesScreen
import com.example.recipecompapp.features.details.ui.RecipeDetailsScreen
import com.example.recipecompapp.features.favorites.ui.FavoritesScreen
import com.example.recipecompapp.core.ui.BottomNavigation
import com.example.recipecompapp.core.navigation.Destination
import com.example.recipecompapp.data.repository.RecipesRepositoryStub
import com.example.recipecompapp.features.details.presentation.RecipeDetailsViewModel
import com.example.recipecompapp.features.favorites.presentation.FavoritesViewModel
import com.example.recipecompapp.features.recipes.presentation.RecipesViewModel
import com.example.recipecompapp.features.recipes.ui.RecipesScreen
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme
import kotlinx.coroutines.delay

@Composable
fun RecipesApp(deepLinkIntent: Intent?) {
    RecipeCompAppTheme {
        val navController = rememberNavController()
        val context = LocalContext.current
        val application = context.applicationContext as Application
        val dataStoreManager = remember { FavoriteDataStoreManager(context) }
        val favoriteCountFlow = remember { dataStoreManager.getFavoriteCountFlow() }
        val repository = remember { RecipesRepositoryStub }

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
                        onCategoryClick = { categoryId, title, imageUrl ->
                            navController.navigate(
                                Destination.Recipes.createRoute(
                                    categoryId,
                                    title,
                                    imageUrl
                                )
                            )
                        },
                    )
                }

                composable(
                    route = Destination.Recipes.route,
                    arguments = listOf(
                        navArgument(Constants.ARG_CATEGORY_ID) { type = NavType.IntType },
                        navArgument(Constants.ARG_CATEGORY_TITLE) { type = NavType.StringType },
                        navArgument(Constants.ARG_CATEGORY_IMAGE_URL) { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val viewModel: RecipesViewModel = viewModel()
                    RecipesScreen(
                        viewModel = viewModel,
                        onRecipeClick = { recipeId, _ ->
                            Log.d("DEBUG", "Клик по рецепту $recipeId")
                            navController.navigate(Destination.RecipeDetails.createRoute(recipeId))
                        }
                    )
                }

                composable(Destination.Favorites.route) { backStackEntry ->
                    val viewModel: FavoritesViewModel = favoritesViewModel(
                        backStackEntry,
                        repository,
                        dataStoreManager
                    )
                    FavoritesScreen(
                        onRecipeClick = { recipeId ->
                            navController.navigate(Destination.RecipeDetails.createRoute(recipeId))
                        },
                        viewModel = viewModel
                    )
                }

                composable(
                    route = Destination.RecipeDetails.route,
                    arguments = listOf(
                        navArgument(Constants.ARG_RECIPE_ID) { type = NavType.IntType })
                ) { backStackEntry ->
                    val viewModel: RecipeDetailsViewModel = recipeDetailsViewModel(
                        backStackEntry,
                        repository,
                        dataStoreManager
                    )
                    RecipeDetailsScreen(
                        viewModel = viewModel,
                        onNavigateBack = { navController.popBackStack() }
                    )
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

@SuppressLint("LocalContextResourcesRead")
@Composable
private fun recipeDetailsViewModel(
    backStackEntry: NavBackStackEntry,
    repository: RecipesRepositoryStub,
    dataStoreManager: FavoriteDataStoreManager
): RecipeDetailsViewModel {
    val context = LocalContext.current
    return viewModel(backStackEntry) {
        RecipeDetailsViewModel(
            savedStateHandle = backStackEntry.savedStateHandle,
            resources = context.resources,
            repository = repository,
            dataStoreManager = dataStoreManager
        )
    }
}

@SuppressLint("LocalContextResourcesRead")
@Composable
private fun favoritesViewModel(
    backStackEntry: NavBackStackEntry,
    repository: RecipesRepositoryStub,
    dataStoreManager: FavoriteDataStoreManager
): FavoritesViewModel {
    val context = LocalContext.current
    return viewModel(backStackEntry) {
        FavoritesViewModel(
            savedStateHandle = backStackEntry.savedStateHandle,
            resources = context.resources,
            repository = repository,
            dataStoreManager = dataStoreManager
        )
    }
}

@Preview
@Composable
fun RecipesAppPreview() {
    RecipeCompAppTheme {
        RecipesApp(deepLinkIntent = null)
    }
}