package com.example.recipecompapp.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipecompapp.data.database.RecipesDatabase
import com.example.recipecompapp.data.database.dao.CategoryDao
import com.example.recipecompapp.data.model.CategoryDto
import com.example.recipecompapp.data.network.api.RecipesApiService
import app.cash.turbine.test
import com.example.recipecompapp.data.database.entity.CategoryEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class RecipesRepositoryIntegrationTest {
    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao
    private val apiService = mockk<RecipesApiService>()

    private lateinit var repository: RecipesRepositoryImpl

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        categoryDao = database.categoryDao()
        repository = RecipesRepositoryImpl(recipesApiService = apiService, database = database)
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        database.close()
    }

    @Test
    fun savesDataToCacheAfterSuccessfulApiCall() = runTest {
        coEvery { apiService.getCategories() } returns listOf(
            CategoryDto(
                id = 1,
                title = "Бургеры",
                description = "",
                imageUrl = ""
            )
        )
        repository.getCategories().test(timeout = 2.seconds) {
            awaitItem()
            val loaded = awaitItem()
            assertEquals("Бургеры", loaded.first().title)
            cancelAndIgnoreRemainingEvents()
        }

        val cached = categoryDao.getCategories().first()

        coVerify(exactly = 1) { apiService.getCategories() }
        assertEquals(1, cached.size)
        assertEquals("Бургеры", cached[0].name)
        assertEquals(1, cached[0].id)
    }

    @Test
    fun returnsCachedDataWhenApiFails() = runTest {
        val cachedCategory = CategoryEntity(
            id = 1,
            name = "Бургеры",
            description = "",
            imageUrl = "burgers.jpg"
        )
        categoryDao.insertCategory(listOf(cachedCategory))

        coEvery { apiService.getCategories() } throws IOException("Network Error")

        val result = repository.getCategories().first()

        coVerify(exactly = 1) { apiService.getCategories() }
        assertEquals(1, result.size)
        assertEquals(cachedCategory.id, result.first().id)
        assertEquals(cachedCategory.name, result.first().title)
    }
}