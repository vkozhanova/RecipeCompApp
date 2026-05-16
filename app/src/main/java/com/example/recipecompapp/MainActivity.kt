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
import com.example.recipecompapp.data.model.CategoryDto
import com.example.recipecompapp.data.model.RecipeDto
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf(null as Intent?)
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()
    private lateinit var threadPool: ExecutorService
    private val jsonParser = Json { ignoreUnknownKeys = true }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        threadPool = Executors.newFixedThreadPool(10)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        intent?.data?.let { _ -> deepLinkIntent = intent }

        thread {
            loadCategories()
        }
        setContent {
            RecipesApp(deepLinkIntent = deepLinkIntent)
        }
    }

    private fun loadCategories() {
        val threadName = Thread.currentThread().name
        val request = Request.Builder()
            .url("https://recipes.androidsprint.ru/api/category")
            .build()

        try {
            okHttpClient.newCall(request).execute().use { response ->
                val body = response.body?.string()
                if (response.isSuccessful && body != null) {
                    val categories = jsonParser.decodeFromString<List<CategoryDto>>(body)
                    Log.i(
                        "!!!",
                        "Количество полученных категорий: ${categories.size} на потоке $threadName"
                    )
                    Log.i("!!!", "Body: $body")

                    categories.forEach { category ->
                        threadPool.execute {
                            loadRecipesForCategory(category)
                        }
                    }
                } else {
                    Log.e("!!!", "Ошибка загрузки категорий, код: ${response.code}, тело:$body")
                }
            }
        } catch (e: Exception) {
            Log.e("!!!", "Исключение при загурзке категорий, ${e.message}", e)
        }
    }

    private fun loadRecipesForCategory(category: CategoryDto) {
        val threadName = Thread.currentThread().name
        val request = Request.Builder()
            .url("https://recipes.androidsprint.ru/api/category/${category.id}/recipes")
            .build()

        try {
            okHttpClient.newCall(request).execute().use { response ->
                val body = response.body?.string()

                if (response.isSuccessful && body != null) {
                    val recipes = jsonParser.decodeFromString<List<RecipeDto>>(body)
                    Log.i(
                        "!!!",
                        "Категория ${category.title}: получено рецептов: ${recipes.size} на потоке $threadName"
                    )
                    Log.i("!!!", "Body: $body")
                } else {
                    Log.e(
                        "!!!",
                        "Ошибка при загрузке  рецептов для ${category.title}. Код: ${response.code}, тело: $body"
                    )
                }
            }
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

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}