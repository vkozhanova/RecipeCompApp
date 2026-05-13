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
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf(null as Intent?)

    private val jsonParser = Json { ignoreUnknownKeys = true }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        intent?.data?.let { _ -> deepLinkIntent = intent }

        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        thread {
            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            try {
                val respondCode = connection.responseCode
                if (respondCode == HttpURLConnection.HTTP_OK) {
                    val responseText = connection.inputStream.bufferedReader().use { it.readText() }

                    Log.d("!!!", "Тело:\n$responseText")

                    val categories = jsonParser.decodeFromString<List<CategoryDto>>(responseText)

                    Log.i("!!!", "Количество полученных категорий: ${categories.size}")
                    categories.forEach { category ->
                        Log.i("!!!", "Категория: ${category.title}")
                    }
                } else {
                    Log.e("!!!", "Ошибка сети. Код ответа: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка при выполнении запроса: ${e.message}", e)
            } finally {
                connection.disconnect()
            }
        }

        setContent {
            RecipesApp(deepLinkIntent = deepLinkIntent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { _ -> deepLinkIntent = intent }

        setIntent(intent)
    }
}