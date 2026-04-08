package com.example.recipecompapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecompapp.R
import com.example.recipecompapp.core.ui.screenheader.ScreenHeader
import com.example.recipecompapp.data.util.FavoriteDataStoreManager
import com.example.recipecompapp.ui.navigation.ShareUtils
import com.example.recipecompapp.ui.recipes.model.IngredientUiModel
import com.example.recipecompapp.ui.recipes.model.RecipeUiModel
import com.example.recipecompapp.ui.theme.RecipeCompAppTheme
import com.example.recipecompapp.ui.theme.recipesAppTypography
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val DEFAULT_SERVINGS = 2

@Composable
fun RecipeDetailsScreen(
    recipe: RecipeUiModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val dataStoreManager = remember { FavoriteDataStoreManager(context) }

    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(recipe.id) {
        isFavorite = dataStoreManager.isFavorite(recipe.id)
    }

    fun onFavoriteClick() {
        coroutineScope.launch {
            isFavorite = dataStoreManager.toggleFavorite(recipe.id)
        }
    }

    RecipeDetailsContent(
        recipe = recipe,
        isFavorite = isFavorite,
        onFavoriteClick = { onFavoriteClick() },
        onSharedClick = { ShareUtils.shareRecipe(context, recipe.id, recipe.title) },
        modifier = modifier,
        )
}

@Composable
fun RecipeDetailsContent(
    recipe: RecipeUiModel,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onSharedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPortions by rememberSaveable { mutableStateOf(DEFAULT_SERVINGS) }

    val scaledIngredients = remember(recipe.ingredients, currentPortions) {
        val multiplier = currentPortions.toDouble() / DEFAULT_SERVINGS
        recipe.ingredients.map { ingredient ->
            val parts = ingredient.amount.split(' ', limit = 2)
            val number = parts.firstOrNull()?.toDoubleOrNull() ?: 0.0
            val unit = if (parts.size > 1) parts[1] else ""
            val newNumber = number * multiplier

            val newAmount = if (unit.isNotBlank()) {
                formatNumber(newNumber) + " $unit"
            } else {
                formatNumber(newNumber)
            }
            ingredient.copy(amount = newAmount)
        }
    }

    Column(
        modifier = modifier
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
                text = currentPortions.toString(),
                style = recipesAppTypography.displayLarge,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onSecondary,
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        PortionsSlider(
            currentPortions = currentPortions,
            onPortionsChange = { currentPortions = it }
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

private fun formatNumber(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        "%.1f".format(value).replace(',', '.')
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailsScreenPreview() {
    RecipeCompAppTheme {
        RecipeDetailsContent(
            recipe = RecipeUiModel(
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
            ),
            isFavorite = true,
            onFavoriteClick = {},
            onSharedClick = {}
        )
    }
}