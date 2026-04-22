package com.example.recipecompapp.features.favorites.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecompapp.core.ui.screenheader.ScreenHeader
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme
import com.example.recipecompapp.R
import com.example.recipecompapp.data.repository.RecipesRepositoryStub
import com.example.recipecompapp.data.local.datastore.FavoriteDataStoreManager
import com.example.recipecompapp.ui.recipes.components.RecipeItem
import com.example.recipecompapp.ui.recipes.model.toUiModel
import kotlinx.coroutines.flow.map

@Composable
fun FavoritesScreen(
    repository: RecipesRepositoryStub,
    favoritesManager: FavoriteDataStoreManager,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteRecipesFlow = remember(favoritesManager, repository) {
        favoritesManager.getFavoriteIdsFlow().map { ids ->
            ids.mapNotNull { id ->
                repository.getRecipeById(id.toIntOrNull() ?: -1)?.toUiModel()
            }
        }
    }

    val favoriteRecipes by favoriteRecipesFlow.collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            imageResId = R.drawable.bcg_favorites,
            badgeText = stringResource(R.string.favorites),
        )

        if (favoriteRecipes.isEmpty()) {
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = favoriteRecipes,
                    key = { recipe -> recipe.id }
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

@Preview
@Composable
fun FavoritesScreenPreview() {
    RecipeCompAppTheme {
        FavoritesScreen(
            repository = RecipesRepositoryStub,
            favoritesManager = FavoriteDataStoreManager(LocalContext.current),
            onRecipeClick = {}
        )
    }
}