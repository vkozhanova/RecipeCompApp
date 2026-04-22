package com.example.recipecompapp.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecompapp.R
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme
import com.example.recipecompapp.ui.theme.recipesAppTypography
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun BottomNavigation(
    onCategoriesClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    favoriteCountFlow: Flow<Int>,
) {
    val favoriteCount by favoriteCountFlow.collectAsState(initial = 0)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(52.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),

        ) {
        NavigationButton(
            text = stringResource(R.string.categories),
            onClick = onCategoriesClick,
            buttonColor = MaterialTheme.colorScheme.tertiary,
            textColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(4.dp))

        NavigationButtonIcon(
            text = stringResource(R.string.favorites),
            onClick = onFavoriteClick,
            buttonColor = MaterialTheme.colorScheme.error,
            textColor = Color.White,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
            counter = favoriteCount,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun NavigationButton(
    text: String,
    onClick: () -> Unit,
    buttonColor: Color,
    textColor: Color,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(36.dp),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            contentColor = textColor,
            containerColor = buttonColor
        ),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
    ) {
        Text(
            text = text,
            style = recipesAppTypography.labelLarge
        )
    }
}

@Composable
fun NavigationButtonIcon(
    text: String,
    onClick: () -> Unit,
    buttonColor: Color,
    textColor: Color,
    shape: RoundedCornerShape,
    counter: Int,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(36.dp),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            contentColor = textColor,
            containerColor = buttonColor
        ),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = text,
                style = recipesAppTypography.labelLarge,
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_favorite),
                contentDescription = "Favorite icon",
                modifier = Modifier.size(24.dp),
            )

            if (counter > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Badge(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier,
                ) {
                    Text(
                        text = counter.toString(),
                        style = recipesAppTypography.labelLarge,
                        modifier = Modifier.padding(horizontal = 4.dp),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun BottomNavigationPreview() {
    RecipeCompAppTheme {
        BottomNavigation(
            onFavoriteClick = {},
            onCategoriesClick = {},
            favoriteCountFlow = flowOf(3)
        )
    }
}