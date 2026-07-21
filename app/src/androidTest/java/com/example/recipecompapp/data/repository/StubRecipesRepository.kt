package com.example.recipecompapp.data.repository

import com.example.recipecompapp.data.model.CategoryDto
import com.example.recipecompapp.data.model.RecipeDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class StubRecipesRepository: RecipesRepository {
    override fun getCategories(): Flow<List<CategoryDto>> =
        flowOf(RecipesRepositoryStub.getCategories())

    override fun getRecipesByCategory(categoryId: Int): Flow<List<RecipeDto>> =
        flowOf(RecipesRepositoryStub.getRecipesByCategoryId(categoryId))

    override suspend fun getRecipesByIds(recipeIds: List<Int>): List<RecipeDto> =
        recipeIds.mapNotNull { id ->
            RecipesRepositoryStub.getRecipeById(id)
        }

    override fun getRecipe(recipeId: Int): Flow<RecipeDto?> =
        flowOf(RecipesRepositoryStub.getRecipeById(recipeId))
}