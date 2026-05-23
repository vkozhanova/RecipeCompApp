package com.example.recipecompapp.core.network.api

import com.example.recipecompapp.data.model.CategoryDto
import com.example.recipecompapp.data.model.RecipeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipesApiService {
    @GET("category")
    suspend fun getCategories(): List<CategoryDto>

    @GET("category/{id}/recipes")
    suspend fun getRecipesByCategory(@Path("id") categoryId: Int): List<RecipeDto>

    @GET("recipe/{id}")
    suspend fun getRecipe(@Path("id") id: Int): RecipeDto
}