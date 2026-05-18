package com.example.recipecompapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.example.recipecompapp.core.network.NetworkConfig.BASE_URL
import com.example.recipecompapp.core.network.api.RecipesApiService
import com.example.recipecompapp.data.model.CategoryDto
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf(null as Intent?)
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
    private val apiService = retrofit.create(RecipesApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        intent?.data?.let { _ -> deepLinkIntent = intent }

        lifecycleScope.launch {
            loadCategories()
        }

        setContent {
            RecipesApp(deepLinkIntent = deepLinkIntent)
        }
    }

    private suspend fun loadCategories() {
        val threadName = Thread.currentThread().name
        try {
            val categories = apiService.getCategories()
            Log.i(
                "!!!",
                "Количество полученных категорий: ${categories.size} на потоке $threadName"
            )

            categories.forEach { category ->
                lifecycleScope.launch {
                    loadRecipesForCategory(category)
                }
            }
        } catch (e: Exception) {
            Log.e("!!!", "Исключение при загрузке категорий, ${e.message}", e)
        }
    }

    private suspend fun loadRecipesForCategory(category: CategoryDto) {
        val threadName = Thread.currentThread().name
        try {
            val recipes = apiService.getRecipesByCategory(category.id)
            Log.i(
                "!!!",
                "Категория ${category.title}: получено рецептов: ${recipes.size} на потоке $threadName"
            )
        } catch (e: Exception) {
            Log.e(
                "!!!",
                "Исключение при запросе рецептов для категории ${category.title}: ${e.message}",
                e
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { _ -> deepLinkIntent = intent }
        setIntent(intent)
    }
}