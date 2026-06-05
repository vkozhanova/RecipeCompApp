package com.example.recipecompapp.data.repository

import android.util.Log
import com.example.recipecompapp.core.network.api.RecipesApiService
import com.example.recipecompapp.data.database.RecipesDatabase
import com.example.recipecompapp.data.database.dao.CategoryDao
import com.example.recipecompapp.data.database.dao.RecipeDao
import com.example.recipecompapp.data.model.CategoryDto
import com.example.recipecompapp.data.model.RecipeDto
import com.example.recipecompapp.data.model.toDto
import com.example.recipecompapp.data.model.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipesRepositoryImpl(
    private val recipesApiService: RecipesApiService,
    private val database: RecipesDatabase
) : RecipesRepository {
    private val categoryDao: CategoryDao = database.categoryDao()
    private val recipeDao: RecipeDao = database.recipeDao()

    override fun getCategories(): Flow<List<CategoryDto>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val freshCategories = recipesApiService.getCategories()
                categoryDao.insertCategory(freshCategories.map { it.toEntity() })
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка получения категорий: ${e.message}", e)
            }
        }
        return categoryDao.getCategories().map { entities -> entities.map { it.toDto() } }
    }

    override fun getRecipesByCategory(categoryId: Int): Flow<List<RecipeDto>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val freshRecipes = recipesApiService.getRecipesByCategory(categoryId)
                val entities = freshRecipes.map { it.toEntity(categoryId) }
                recipeDao.insertRecipesList(entities)
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка получения рецептов для категории $categoryId: ${e.message}", e)
            }
        }
        return recipeDao.getRecipesList(categoryId.toString()).map { entities ->
            entities.map { it.toDto() }
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