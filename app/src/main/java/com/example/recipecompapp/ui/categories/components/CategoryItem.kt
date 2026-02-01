package com.example.recipecompapp.ui.categories.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.recipecompapp.ui.theme.recipesAppTypography

@Composable
fun CategoryItem(
    image: String,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageRequest = remember(image) {
        ImageRequest.Builder(context)
            .data(image)
            .crossfade(true)
            .build()
    }

    Card(
        modifier = modifier
            .size(width = 156.dp, height = 220.dp)
            .clickable(onClick = onClick)
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
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
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
                    .height(90.dp)
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = title.uppercase(),
                        style = recipesAppTypography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.padding(bottom = 8.dp))

                    Text(
                        text = description,
                        style = recipesAppTypography.bodySmall,
                        modifier = Modifier.fillMaxHeight(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3,
                        color = MaterialTheme.colorScheme.onPrimary


                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoryItemPreview() {
    CategoryItem(
        image = "",
        title = "Бургеры",
        description = "Рецепты всех популярных видов бургеров",
        onClick = {},
        modifier = Modifier.padding(16.dp)
    )
}