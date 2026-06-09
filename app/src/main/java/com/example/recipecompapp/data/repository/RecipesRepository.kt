package com.example.recipecompapp.data.repository

import com.example.recipecompapp.data.model.CategoryDto
import com.example.recipecompapp.data.model.RecipeDto
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun getCategories(): Flow<List<CategoryDto>>

    fun getRecipesByCategory(categoryId: Int): Flow<List<RecipeDto>>

    fun getRecipe(recipeId: Int): Flow<RecipeDto?>

    suspend fun getRecipesByIds(recipeIds: List<Int>): List<RecipeDto>
}