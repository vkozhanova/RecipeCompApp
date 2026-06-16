package com.example.recipecompapp.data.repository

import android.util.Log
import com.example.recipecompapp.data.network.api.RecipesApiService
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipesRepositoryImpl(
    private val recipesApiService: RecipesApiService,
    private val database: RecipesDatabase
) : RecipesRepository {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val categoryDao: CategoryDao = database.categoryDao()
    private val recipeDao: RecipeDao = database.recipeDao()

    override fun getCategories(): Flow<List<CategoryDto>> {
        scope.launch {
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
        scope.launch {
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

    override suspend fun getRecipesByIds(recipeIds: List<Int>): List<RecipeDto> {
        return withContext(Dispatchers.IO) {
            val entities = recipeDao.getRecipesByIdsList(recipeIds)
            entities.map { it.toDto() }
        }
    }

    override fun getRecipe(recipeId: Int): Flow<RecipeDto?> {
        scope.launch {
            try {
                val freshRecipe = recipesApiService.getRecipe(recipeId)
                val existingRecipe = recipeDao.getRecipeById(recipeId).firstOrNull()
                val categoryId = existingRecipe?.categoryId?.toIntOrNull()
                if (categoryId != null) {
                    val entity = freshRecipe.toEntity(categoryId)
                    recipeDao.insertRecipe(entity)
                } else {
                    Log.e("!!!", "Рецепт $recipeId не сохранён: неизвестна категория")
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка обновления рецепта $recipeId: ${e.message}", e)
            }
        }
        return recipeDao.getRecipeById(recipeId).map { it?.toDto() }
    }
}