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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecompapp.data.model.CategoryDto
import com.example.recipecompapp.R
import com.example.recipecompapp.core.ui.screenheader.ScreenHeader
import com.example.recipecompapp.data.model.RecipeDto
import com.example.recipecompapp.data.repository.RecipesRepository
import com.example.recipecompapp.features.categories.presentation.CategoriesViewModel
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun CategoriesScreen(
    repository: RecipesRepository,
    modifier: Modifier = Modifier,
    onCategoryClick: (Int, String, String) -> Unit = { _, _, _ -> }
) {
    val viewModel: CategoriesViewModel = remember(repository) {
        CategoriesViewModel(repository)
    }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                CircularProgressIndicator()
            }
        } else {
            uiState.error?.let { error ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error
                    )
                }
            } ?: run {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
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
        CategoriesScreen(
            repository = object : RecipesRepository {
                override fun getCategories(): Flow<List<CategoryDto>> = flowOf(emptyList())
                override fun getRecipesByCategory(categoryId: Int): Flow<List<RecipeDto>> =
                    flowOf(emptyList())

                override suspend fun getRecipe(recipeId: Int): RecipeDto = RecipeDto(
                    id = recipeId,
                    title = "Mock",
                    imageUrl = "",
                    ingredients = emptyList(),
                    method = emptyList()
                )
            }
        )
    }
}