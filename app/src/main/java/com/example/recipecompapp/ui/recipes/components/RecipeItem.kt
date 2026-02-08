package com.example.recipecompapp.ui.recipes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipecompapp.R
import com.example.recipecompapp.ui.recipes.model.RecipeUiModel
import com.example.recipecompapp.ui.theme.recipesAppTypography
import kotlin.Int

@Composable
fun RecipeItem(
    recipe: RecipeUiModel,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (recipe.id < 0 || recipe.title.isBlank()) {
        return
    }

    val context = LocalContext.current
    val imageRequest = remember(recipe.id) {
        ImageRequest.Builder(context)
            .data(recipe.imageUrl)
            .crossfade(true)
            .build()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(132.dp)
            .clickable{onClick(recipe.id)}
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                clip = true,
                ambientColor = MaterialTheme.colorScheme.onSurfaceVariant,
                spotColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            AsyncImage(
                model = imageRequest,
                contentDescription = recipe.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp
                        )
                    ),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.img_error),
                error = painterResource(R.drawable.img_error)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(color = MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    text = recipe.title.uppercase(),
                    style = recipesAppTypography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterStart),
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview
@Composable
fun RecipeItemPreview() {
    RecipeItem(
        recipe = RecipeUiModel(
            id = 1,
            title = "Чизбургер",
            ingredients = emptyList(),
            method = emptyList(),
            imageUrl = "",
        ),
        onClick = {},
        modifier = Modifier.padding(16.dp)
    )
}