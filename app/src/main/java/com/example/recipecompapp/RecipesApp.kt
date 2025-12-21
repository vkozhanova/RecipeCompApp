package com.example.recipecompapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme

@Composable
fun RecipesApp() {
        RecipeCompAppTheme {
            Scaffold { paddingValues ->
                Text(
                    text = "Recipes App",
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }

@Preview
@Composable
fun RecipesAppPreview() {
    RecipeCompAppTheme {
        RecipesApp()
    }
}