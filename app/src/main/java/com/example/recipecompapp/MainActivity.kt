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
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf(null as Intent?)
    private lateinit var threadPool: ExecutorService
    private val jsonParser = Json { ignoreUnknownKeys = true }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        threadPool = Executors.newFixedThreadPool(10)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        intent?.data?.let { _ -> deepLinkIntent = intent }

        threadPool.execute {
            val threadName = Thread.currentThread().name
            Log.i("!!!", "Выполняю запрос на потоке: $threadName")

            var connection: HttpURLConnection? = null
            try {
                val url = URL("https://recipes.androidsprint.ru/api/category")
                connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 3000
                connection.readTimeout = 3000

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                    val categories = jsonParser.decodeFromString<List<CategoryDto>>(responseText)

                    Log.i(
                        "!!!",
                        "Количество полученных категорий: ${categories.size} на потоке $threadName"
                    )

                    categories.forEach { category ->
                        threadPool.execute {
                            loadRecipesForCategory(category)
                        }
                    }
                } else {
                    val errorText = connection.errorStream?.bufferedReader()?.use { it.readText() }
                    Log.e("!!!", "Ошибка загрузки категорий. Код: $responseCode, тело: $errorText")
                }
            } catch (e: Exception) {
                Log.e("!!!", "Исключение при запросе категорий: ${e.message}", e)
            } finally {
                connection?.disconnect()
            }
        }

        setContent {
            RecipesApp(deepLinkIntent = deepLinkIntent)
        }
    }

    private fun loadRecipesForCategory(category: CategoryDto) {
        val threadName = Thread.currentThread().name
        Log.i(
            "!!!",
            "Запрос рецептов для категории ${category.title} (id:${category.id}) на потоке: $threadName"
        )
        var connection: HttpURLConnection? = null

        try {
            val url = URL("https://recipes.androidsprint.ru/api/category/${category.id}/recipes")
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 3000
            connection.readTimeout = 3000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val responseText = connection.inputStream.bufferedReader().use { it.readText() }
                val recipes = jsonParser.decodeFromString<List<RecipeDto>>(responseText)

                Log.i(
                    "!!!",
                    "Категория ${category.title}: получено рецептов: ${recipes.size} на потоке $threadName"
                )
            } else {
                val errorText = connection.errorStream?.bufferedReader()?.use { it.readText() }
                Log.e(
                    "!!!",
                    "Ошибка при загрузке  рецептов для ${category.title}. Код: $responseCode, тело: $errorText"
                )
            }
        } catch (e: Exception) {
            Log.e(
                "!!!",
                "Исключение при запросе рецептов для категории ${category.title}: ${e.message}",
                e
            )
        } finally {
            connection?.disconnect()
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