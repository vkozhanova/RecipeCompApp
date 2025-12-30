package com.example.recipecompapp.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecompapp.R
import com.example.recipecompapp.ui.theme.recipesAppTypography

@Composable
fun BottomNavigation(
    onCategoriesClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(52.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),

        ) {
        NavigationButton(
            text = "Категории",
            onClick = onCategoriesClick,
            buttonColor = MaterialTheme.colorScheme.tertiary,
            textColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.width(4.dp))

        NavigationButtonIcon(
            text = "Избранное",
            onClick = onFavoriteClick,
            buttonColor = MaterialTheme.colorScheme.error,
            textColor = Color.White,
            shape = RoundedCornerShape(8.dp),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = recipesAppTypography.labelLarge,
                modifier = Modifier.weight(1f),
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_favorite),
                contentDescription = "Favorite icon",
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Preview
@Composable
fun BottomNavigationPreview() {
    BottomNavigation(
        onFavoriteClick = {},
        onCategoriesClick = {}
    )
}