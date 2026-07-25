package com.example.recipecompapp.features.recipes.screen

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class RecipesComposeScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<RecipesComposeScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("recipes_screen") }
    ){
    val loadingIndicator: KNode = child { hasTestTag("loading_indicator") }
    val errorMessage: KNode = child { hasTestTag("error_message") }
    val emptyState: KNode = child { hasTestTag("empty_state") }
}