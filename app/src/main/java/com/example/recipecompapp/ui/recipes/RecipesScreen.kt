package com.example.recipecompapp.ui.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecompapp.R
import com.example.recipecompapp.core.ui.screenheader.ScreenHeader
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme

@Composable
fun RecipesScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            imageResId = R.drawable.bcg_categories,
            badgeText = "Рецепты",
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Text(
            text = "Скоро здесь будут рецепты"
        )
    }
}


@Preview
@Composable
fun RecipesScreenPreview() {
    RecipeCompAppTheme {
        RecipesScreen()
    }
}
