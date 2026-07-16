package com.example.recipecompapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipecompapp.core.Constants
import com.example.recipecompapp.core.Constants.DEEP_LINK_BASE_URL
import com.example.recipecompapp.core.Constants.DEEP_LINK_SCHEME
import com.example.recipecompapp.features.categories.ui.CategoriesScreen
import com.example.recipecompapp.features.details.ui.RecipeDetailsScreen
import com.example.recipecompapp.features.favorites.ui.FavoritesScreen
import com.example.recipecompapp.features.bottom.ui.BottomNavigation
import com.example.recipecompapp.core.navigation.Destination
import com.example.recipecompapp.core.navigation.ShareUtils
import com.example.recipecompapp.features.bottom.presentation.BottomNavigationViewModel
import com.example.recipecompapp.features.details.presentation.RecipeDetailsViewModel
import com.example.recipecompapp.features.favorites.presentation.FavoritesViewModel
import com.example.recipecompapp.features.recipes.presentation.model.RecipeUiModel
import com.example.recipecompapp.features.recipes.ui.RecipesScreen
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
fun RecipesApp(
    deepLinkIntent: Intent?,
    onDeepLinkConsumed: () -> Unit
) {
    RecipeCompAppTheme {
        val navController = rememberNavController()
        val context = LocalContext.current

        val bottomNavigationViewModel: BottomNavigationViewModel = hiltViewModel()
        val favoriteCountFlow = bottomNavigationViewModel.favoriteCountFlow

        LaunchedEffect(deepLinkIntent) {
            deepLinkIntent?.data?.let { uri ->
                val recipeId = parseRecipeIdFromUri(uri)
                if (recipeId != null) {
                    navController.navigate(Destination.RecipeDetails.createRoute(recipeId))
                }
                onDeepLinkConsumed()
            }
        }

        val onCategoriesClick = remember(navController) {
            {
                navController.navigate(Destination.Categories.route) {
                    popUpTo(Destination.Categories.route) { inclusive = true }
                }
            }
        }
        val onFavoriteClick = remember(navController) {
            {
                navController.navigate(Destination.Favorites.route) {
                    popUpTo(Destination.Favorites.route) { inclusive = true }
                }
            }
        }

        Scaffold(
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = onCategoriesClick,
                    onFavoriteClick = onFavoriteClick,
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
                    val onCategoryClick = remember(navController) {
                        { categoryId: Int, title: String, imageUrl: String ->
                            navController.navigate(
                                Destination.Recipes.createRoute(
                                    categoryId,
                                    title,
                                    imageUrl
                                )
                            )
                        }
                    }
                    CategoriesScreen(
                        onCategoryClick = onCategoryClick
                    )
                }

                composable(
                    route = Destination.Recipes.route,
                    arguments = listOf(
                        navArgument(Constants.ARG_CATEGORY_ID) { type = NavType.IntType },
                        navArgument(Constants.ARG_CATEGORY_TITLE) { type = NavType.StringType },
                        navArgument(Constants.ARG_CATEGORY_IMAGE_URL) {
                            type = NavType.StringType
                        }
                    )
                ) { _ ->
                    val onRecipeClick = remember(navController) {
                        { recipeId: Int, _: RecipeUiModel ->
                            Log.d("DEBUG", "Клик по рецепту $recipeId")
                            navController.navigate(
                                Destination.RecipeDetails.createRoute(
                                    recipeId
                                )
                            )
                        }
                    }
                    RecipesScreen(
                        onRecipeClick = onRecipeClick
                    )
                }

                composable(Destination.Favorites.route) {
                    val viewModel: FavoritesViewModel = hiltViewModel()
                    val uiState by viewModel.uiState.collectAsState()
                    FavoritesScreen(
                        uiState = uiState,
                        onRecipeClick = { recipeId ->
                            navController.navigate(
                                Destination.RecipeDetails.createRoute(
                                    recipeId
                                )
                            )
                        },
                    )
                }

                composable(
                    route = Destination.RecipeDetails.route,
                    arguments = listOf(
                        navArgument(Constants.ARG_RECIPE_ID) { type = NavType.IntType })
                ) { _ ->
                    val viewModel: RecipeDetailsViewModel = hiltViewModel()
                    val uiState by viewModel.uiState.collectAsState()
                    RecipeDetailsScreen(
                        uiState = uiState,
                        onServingsChange = { viewModel.updatePortions(it) },
                        onFavoriteClick = { viewModel.toggleFavorite() },
                        onSharedClick = remember(uiState.recipe) {
                            {
                                uiState.recipe?.let { recipe ->
                                    ShareUtils.shareRecipe(context, recipe.id, recipe.title)
                                }
                            }
                        },
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