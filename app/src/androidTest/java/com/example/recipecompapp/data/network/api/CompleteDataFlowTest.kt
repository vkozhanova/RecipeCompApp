package com.example.recipecompapp.data.network.api

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipecompapp.data.database.RecipesDatabase
import com.example.recipecompapp.data.repository.RecipesRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import okhttp3.ConnectionSpec
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@RunWith(AndroidJUnit4::class)
class CompleteDataFlowTest {
    private lateinit var database: RecipesDatabase
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        mockWebServer = MockWebServer().also { it.start() }
    }

    @After
    fun tearDown() {
        database.close()
        mockWebServer.shutdown()
    }

    @Test
    fun categoriesAreLoadedFromApiAndStoredInCache() = runBlocking {
        val json = """
            [
                {
                    "id": 1,
                    "title": "Завтраки",
                    "description": "Лёгкие блюда",
                    "imageUrl": "breakfast.jpg"
                }
            ]
        """.trimIndent()
        mockWebServer.enqueue(
            MockResponse()
                .setBody(json)
                .setResponseCode(200)
        )

        val client = OkHttpClient.Builder()
            .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT))
            .build()

        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RecipesApiService::class.java)

        val repository = RecipesRepositoryImpl(recipesApiService = apiService, database = database)

        val job = launch {
            repository.getCategories().collect { }
        }

        withTimeout(5.seconds) {
            while (database.categoryDao().getCategories().first().isEmpty()) {
                delay(100.milliseconds)
            }
        }
        job.cancel()

        val cached = database.categoryDao().getCategories().first()
        assertEquals(1, mockWebServer.requestCount)
        assertEquals(1, cached.size)
        with(cached.first()) {
            assertEquals(1, id)
            assertEquals("Завтраки", name)
            assertEquals("Лёгкие блюда", description)
            assertEquals("breakfast.jpg", imageUrl)
        }
    }
}