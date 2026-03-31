package com.example.recipecompapp.core.ui.screenheader

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipecompapp.R
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme
import com.example.recipecompapp.ui.theme.recipesAppTypography

@Composable
fun ScreenHeader(
    imageUrl: String? = null,
    imageResId: Int,
    badgeText: String,
    showShareButton: Boolean = false,
    onSharedClick: () -> Unit = {},
    showFavoriteButton: Boolean = false,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(224.dp)
            .background(Color.White)
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                error = painterResource(id = R.drawable.img_error)
            )
        } else {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (showShareButton) {
            Icon(
                painter = painterResource(R.drawable.share),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
                    .size(40.dp)
                    .clickable { onSharedClick() }
            )
        }

        if (showFavoriteButton) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 60.dp, end = 16.dp)
            ) {
                Crossfade(
                    targetState = isFavorite,
                    animationSpec = tween(300),
                    label = "fav_animation"
                ) { isFavoriteState ->
                    val heartIcon = ImageVector.vectorResource(
                        id = if (isFavoriteState) R.drawable.ic_heart else R.drawable.ic_heart_empty
                    )
                    val painter = rememberVectorPainter(image = heartIcon)
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { onFavoriteClick() }
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Text(
                text = badgeText.uppercase(),
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary,
                style = recipesAppTypography.displayLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenHeaderPreview() {
    RecipeCompAppTheme {
        ScreenHeader(
            imageResId = R.drawable.bcg_categories,
            showShareButton = true,
            showFavoriteButton = true,
            isFavorite = true,
            badgeText = "Заголовок хедера"
        )
    }
}