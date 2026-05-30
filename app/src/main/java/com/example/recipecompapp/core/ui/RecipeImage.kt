package com.example.recipecompapp.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.recipecompapp.R

@Composable
fun RecipeImage(
    imageUrl: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val errorImage = painterResource(R.drawable.img_error)
    val placeholder = painterResource(R.drawable.img_placeholder)
    val context = LocalContext.current

    if (imageUrl.isNullOrEmpty()) {
        Box(modifier, contentAlignment = Alignment.Center) {
            Image(
                painter = errorImage,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = contentScale
            )
        }
        return
    }

    val imageRequest = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(300)
            .build()
    }

    AsyncImage(
        model = imageRequest,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        placeholder = placeholder,
        error = errorImage
    )
}