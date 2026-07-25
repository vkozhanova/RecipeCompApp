package com.example.recipecompapp.features.categories.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.recipecompapp.R
import com.example.recipecompapp.core.ui.screenheader.ScreenHeader
import com.example.recipecompapp.features.categories.presentation.CategoriesViewModel
import com.example.recipecompapp.features.categories.presentation.model.CategoriesUiState
import com.example.recipecompapp.features.categories.presentation.model.CategoryUiModel
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme

@Composable
fun CategoriesScreen(
    onCategoryClick: (Int, String, String) -> Unit = { _, _, _ -> }
) {
    val viewModel: CategoriesViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    CategoriesContent(
        uiState = uiState,
        onCategoryClick = onCategoryClick
    )
}

@Composable
fun CategoriesContent(
    uiState: CategoriesUiState,
    modifier: Modifier = Modifier,
    onCategoryClick: (Int, String, String) -> Unit = { _, _, _ -> }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("categories_screen")
    ) {
        ScreenHeader(
            imageResId = R.drawable.bcg_categories,
            badgeText = stringResource(R.string.categories),
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
            }
        } else {
            uiState.error?.let { error ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.testTag("error_message")
                    )
                }
            } ?: run {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .testTag("categories_grid"),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = uiState.categories,
                        key = { category -> category.id }
                    ) { category ->
                        CategoryItem(
                            image = category.imageUrl,
                            title = category.title,
                            description = category.description,
                            onClick = {
                                onCategoryClick(category.id, category.title, category.imageUrl)
                            },
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoriesScreenPreview() {
    RecipeCompAppTheme {
        CategoriesContent(
            uiState = CategoriesUiState(
                isLoading = false,
                categories = listOf(
                    CategoryUiModel(1, "Бургеры", "Классические", ""),
                    CategoryUiModel(2, "Пицца", "Итальянская", "")
                )
            ),
            onCategoryClick = { _, _, _ -> }
        )
    }
}