package com.example.recipecompapp.ui.navigation

sealed class Destination(val route: String) {
    object Categories : Destination("categories")
    object Recipes : Destination("recipes/{categoryId}") {
        fun createRoute(categoryId: Int) = "recipes/$categoryId"
    }

    object Favorites : Destination("favorites")

    object RecipeDetails : Destination("recipe/{recipeId}") {
        fun createRoute(recipeId: Int) = "recipe/$recipeId"
    }
}

const val KEY_RECIPE_OBJECT = "recipe_object"