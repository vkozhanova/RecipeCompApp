package com.example.recipecompapp.data.repository

import com.example.recipecompapp.data.model.CategoryDto
import com.example.recipecompapp.data.model.RecipeDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.milliseconds

class StubRecipesRepository: RecipesRepository {
    override fun getCategories(): Flow<List<CategoryDto>> = flow {
        delay(300.milliseconds)
        emit(RecipesRepositoryStub.getCategories())
    }

    override fun getRecipesByCategory(categoryId: Int): Flow<List<RecipeDto>> = flow {
        delay(1500.milliseconds)
        emit(RecipesRepositoryStub.getRecipesByCategoryId(categoryId))
    }

    override suspend fun getRecipesByIds(recipeIds: List<Int>): List<RecipeDto> =
        recipeIds.mapNotNull { id ->
            RecipesRepositoryStub.getRecipeById(id)
        }

    override fun getRecipe(recipeId: Int): Flow<RecipeDto?> = flow {
        delay(300.milliseconds)
       emit(RecipesRepositoryStub.getRecipeById(recipeId))
    }
}