package com.example.recipecompapp.features.recipes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecompapp.R
import com.example.recipecompapp.core.ui.screenheader.ScreenHeader
import com.example.recipecompapp.features.recipes.presentation.RecipesViewModel
import com.example.recipecompapp.ui.recipes.components.RecipeItem
import com.example.recipecompapp.ui.recipes.model.RecipeUiModel
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme

@Composable
fun RecipesScreen(
    viewModel: RecipesViewModel,
    onRecipeClick: (Int, RecipeUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            imageUrl = uiState.categoryImageUrl.takeIf { it.isNotEmpty() },
            badgeText = uiState.categoryTitle.ifEmpty { stringResource(R.string.recipes) }
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
                    Text(text = error, color = MaterialTheme.colorScheme.error)
                }
            } ?: run {
                if (uiState.recipes.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_recipes_categories),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(
                            items = uiState.recipes,
                            key = { it.id }
                        ) { recipe ->
                            RecipeItem(
                                recipe = recipe,
                                onClick = { onRecipeClick(recipe.id, recipe) },
                                modifier = Modifier.fillMaxWidth()
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
fun RecipesScreenPreview() {
    RecipeCompAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ScreenHeader(
                imageResId = R.drawable.bcg_categories,
                badgeText = "Бургеры"
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
            ) {
                items(5) { index ->
                    RecipeItem(
                        recipe = RecipeUiModel(
                            id = index,
                            title = when (index) {
                                0 -> "чизбургер"
                                1 -> "классический гамбургер"
                                2 -> "бургер с грибами и сыром"
                                3 -> "вегетерианский бургер"
                                else -> "Острый бургер с чили"
                            }.uppercase(),
                            ingredients = emptyList(),
                            method = emptyList(),
                            imageUrl = "",
                            isFavorite = index % 2 == 0
                        ),
                        onClick = {},
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
