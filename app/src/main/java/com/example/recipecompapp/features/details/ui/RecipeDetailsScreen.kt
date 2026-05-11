package com.example.recipecompapp.features.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecompapp.R
import com.example.recipecompapp.core.ui.screenheader.ScreenHeader
import com.example.recipecompapp.core.navigation.ShareUtils
import com.example.recipecompapp.features.details.presentation.RecipeDetailsViewModel
import com.example.recipecompapp.ui.recipes.model.IngredientUiModel
import com.example.recipecompapp.features.recipes.presentation.model.RecipeUiModel
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme
import com.example.recipecompapp.ui.theme.recipesAppTypography
import kotlin.math.roundToInt

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                uiState.error?.let { error ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } ?: run {
                    val recipe = uiState.recipe
                    if (recipe == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = stringResource(R.string.recipe_not_found))
                        }
                    } else {
                        RecipeDetailsContent(
                            recipe = recipe,
                            servings = uiState.currentPortions,
                            scaledIngredients = uiState.scaledIngredients,
                            isFavorite = uiState.isFavorite,
                            onServingsChange = { viewModel.updatePortions(it) },
                            onFavoriteClick = { viewModel.toggleFavorite() },
                            onSharedClick = {
                                ShareUtils.shareRecipe(
                                    context,
                                    recipe.id,
                                    recipe.title
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeDetailsContent(
    recipe: RecipeUiModel,
    servings: Int,
    scaledIngredients: List<IngredientUiModel>,
    isFavorite: Boolean,
    onServingsChange: (Int) -> Unit,
    onFavoriteClick: () -> Unit,
    onSharedClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        ScreenHeader(
            imageUrl = recipe.imageUrl,
            imageResId = R.drawable.bcg_categories,
            badgeText = recipe.title,
            showShareButton = true,
            onSharedClick = onSharedClick,
            showFavoriteButton = true,
            isFavorite = isFavorite,
            onFavoriteClick = onFavoriteClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.ingredients).uppercase(),
            style = recipesAppTypography.displayLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.portions_label),
                style = recipesAppTypography.displayLarge,
                color = MaterialTheme.colorScheme.onSecondary,
            )
            Text(
                text = servings.toString(),
                style = recipesAppTypography.displayLarge,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        PortionsSlider(
            currentPortions = servings,
            onPortionsChange = { onServingsChange(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        IngredientsList(
            ingredients = scaledIngredients,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.instructions).uppercase(),
            style = recipesAppTypography.displayLarge,
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.primary,
        )

        InstructionsList(
            instructions = recipe.method,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun PortionsSlider(
    currentPortions: Int,
    onPortionsChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Slider(
        value = currentPortions.toFloat(),
        onValueChange = { onPortionsChange(it.roundToInt()) },
        valueRange = 1f..12f,
        steps = 10,
        modifier = modifier.padding(horizontal = 16.dp),
    )
}

@Composable
fun IngredientsList(
    ingredients: List<IngredientUiModel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
    ) {
        ingredients.forEachIndexed { index, ingredient ->
            IngredientItem(
                ingredient = ingredient,
                modifier = Modifier.fillMaxWidth(),
                isFirst = index == 0,
                isLast = index == ingredients.lastIndex,
            )

            if (index < ingredients.lastIndex) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

@Composable
fun IngredientItem(
    ingredient: IngredientUiModel,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    val verticalPadding = 8.dp
    val topPadding = if (isFirst) 0.dp else verticalPadding
    val bottomPadding = if (isLast) 0.dp else verticalPadding

    Row(
        modifier = modifier
            .padding(top = topPadding, bottom = bottomPadding)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = ingredient.name.uppercase(),
            style = recipesAppTypography.bodyMedium,
        )
        Text(
            text = ingredient.amount.uppercase(),
            style = recipesAppTypography.bodyMedium,
        )
    }
}

@Composable
fun InstructionsList(
    instructions: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
    ) {
        instructions.forEachIndexed { index, instruction ->
            val isFirst = index == 0
            val isLast = index == instructions.lastIndex
            val verticalPadding = 8.dp
            val topPadding = if (isFirst) 0.dp else verticalPadding
            val bottomPadding = if (isLast) 0.dp else verticalPadding

            Text(
                text = "${index + 1}. $instruction",
                style = recipesAppTypography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = topPadding, bottom = bottomPadding)
            )

            if (index < instructions.lastIndex) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailsScreenPreview() {
    val recipe = RecipeUiModel(
        id = 1,
        title = "Классический бургер",
        ingredients = listOf(
            IngredientUiModel(name = "Говяжий фарш", amount = "0.5 кг"),
            IngredientUiModel(name = "Лук", amount = "1 шт"),
            IngredientUiModel(name = "Чеснок", amount = "2 зубч")
        ),
        method = listOf(
            "Смешайте фарш с луком и чесноком",
            "Сформируйте котлеты",
            "Обжарьте на сковороде"
        ),
        imageUrl = null,
    )
    RecipeCompAppTheme {
        RecipeDetailsContent(
            recipe = recipe,
            servings = 2,
            scaledIngredients = recipe.ingredients,
            isFavorite = true,
            onServingsChange = {},
            onFavoriteClick = {},
            onSharedClick = {}
        )
    }
}
