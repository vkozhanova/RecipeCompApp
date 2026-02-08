package com.example.recipecompapp.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecompapp.R
import com.example.recipecompapp.core.ui.screenheader.ScreenHeader
import com.example.recipecompapp.data.repository.RecipesRepositoryStub
import com.example.recipecompapp.ui.categories.components.CategoryItem
import com.example.recipecompapp.ui.categories.model.toUiModel
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (Int, String) -> Unit = {_, _ ->}
) {
    val categories = RecipesRepositoryStub.getCategories()
    val uiCategories = categories.map { it.toUiModel() }

    RecipeCompAppTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ScreenHeader(
                imageResId = R.drawable.bcg_categories,
                badgeText = "Категории",
            )
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
                    items = uiCategories,
                    key = { category -> category.id }
                ) { category ->
                    CategoryItem(
                        image = category.imageUrl,
                        title = category.title,
                        description = category.description,
                        onClick = {
                            onCategoryClick(category.id, category.title) },
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoriesScreenPreview() {
    RecipeCompAppTheme {
        CategoriesScreen()
    }
}