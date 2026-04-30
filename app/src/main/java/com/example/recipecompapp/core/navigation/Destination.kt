package com.example.recipecompapp.core.navigation

import com.example.recipecompapp.core.Constants
import java.net.URLEncoder

sealed class Destination(val route: String) {
    object Categories : Destination("categories")
    object Recipes :
        Destination("recipes/{${Constants.ARG_CATEGORY_ID}}/{${Constants.ARG_CATEGORY_TITLE}}/{${Constants.ARG_CATEGORY_IMAGE_URL}}") {
        fun createRoute(categoryId: Int, categoryTitle: String, categoryImageUrl: String): String {
            val encodedTitle = URLEncoder.encode(categoryTitle, "UTF-8")
            val encodedImageUrl = URLEncoder.encode(categoryImageUrl, "UTF-8")
            return "recipes/$categoryId/$encodedTitle/$encodedImageUrl"
        }
    }

    object Favorites : Destination("favorites")

    object RecipeDetails : Destination("recipe/{${Constants.ARG_RECIPE_ID}}") {
        fun createRoute(recipeId: Int) = "recipe/$recipeId"
    }
}

const val KEY_RECIPE_OBJECT = "recipe_object"