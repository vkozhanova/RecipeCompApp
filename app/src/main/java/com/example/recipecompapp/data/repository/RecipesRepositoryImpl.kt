package com.example.recipecompapp.data.repository

import android.util.Log
import com.example.recipecompapp.core.network.api.RecipesApiService
import com.example.recipecompapp.data.model.CategoryDto
import com.example.recipecompapp.data.model.RecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipesRepositoryImpl(
    private val recipesApiService: RecipesApiService
) : RecipesRepository {
    override suspend fun getCategories(): List<CategoryDto> {
        return withContext(Dispatchers.IO) {
            try {
                recipesApiService.getCategories()
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка получения категорий: ${e.message}", e)
                emptyList()
            }
        }
    }

    override suspend fun getRecipesByCategory(categoryId: Int): List<RecipeDto> {
        return withContext(Dispatchers.IO) {
            try {
               recipesApiService.getRecipesByCategory(categoryId)
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка получения рецептов для категории $categoryId: ${e.message}", e)
                emptyList()
            }
        }
    }

    override suspend fun getRecipe(recipeId: Int): RecipeDto {
        return withContext(Dispatchers.IO) {
            try {
                recipesApiService.getRecipe(recipeId)
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка при получении рецепта $recipeId: ${e.message}", e)
                throw e
            }
        }
    }
}