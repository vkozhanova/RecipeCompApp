package com.example.recipecompapp.data.repository

import com.example.recipecompapp.data.model.CategoryDto
import com.example.recipecompapp.data.model.RecipeDto

interface RecipesRepository {
    suspend fun getCategories(): List<CategoryDto>

    suspend fun getRecipesByCategory(categoryId: Int): List<RecipeDto>

    suspend fun getRecipe(recipeId: Int): RecipeDto
}