package com.example.recipecompapp.ui.navigation

sealed class Destination(val route: String) {
    object Categories : Destination("categories")
    object Recipes : Destination("recipes/{categoryId}") {
        fun createRoute(categoryId: Int) = "recipes/$categoryId"
    }
    object Favorites : Destination("favorites")
}