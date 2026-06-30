package com.example.recipecompapp.features.favorites.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecompapp.core.ui.screenheader.ScreenHeader
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme
import com.example.recipecompapp.R
import com.example.recipecompapp.features.favorites.presentation.model.FavoritesUiState
import com.example.recipecompapp.features.recipes.presentation.model.RecipeUiModel
import com.example.recipecompapp.features.recipes.ui.RecipeItem

@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            imageResId = R.drawable.bcg_favorites,
            badgeText = stringResource(R.string.favorites),
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            uiState.error?.let { error ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } ?: run {
                if (uiState.isEmpty) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_favorites),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.recipes,
                            key = { it.id }
                        ) { recipe ->
                            RecipeItem(
                                recipe = recipe,
                                onClick = { onRecipeClick(recipe.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun FavoritesScreenPreview() {
    RecipeCompAppTheme {
        FavoritesScreen(
            uiState = FavoritesUiState(
                isLoading = false,
                recipes = listOf(
                    RecipeUiModel(1, "Чизбургер", emptyList(), emptyList(), "", true),
                    RecipeUiModel(2, "Гамбургер", emptyList(), emptyList(), "", false)
                )
            ),
            onRecipeClick = {}
        )
    }
}
