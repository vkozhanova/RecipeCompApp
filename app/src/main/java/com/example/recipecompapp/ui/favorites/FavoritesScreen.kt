package com.example.recipecompapp.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecompapp.core.ui.screenheader.ScreenHeader
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme
import com.example.recipecompapp.R

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier
) {
    RecipeCompAppTheme {
       Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
           ScreenHeader(
               imageResId = R.drawable.bcg_favorites,
               badgeText = "Избранное",
           )
           LazyColumn(
               modifier = Modifier.fillMaxWidth().padding(16.dp)
           ) {

           }
        }
    }
}

@Preview
@Composable
fun FavoritesScreenPreview() {
    RecipeCompAppTheme {
        FavoritesScreen()
    }
}