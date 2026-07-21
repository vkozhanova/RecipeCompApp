package com.example.recipecompapp.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipecompapp.data.database.RecipesDatabase
import com.example.recipecompapp.data.database.dao.CategoryDao
import com.example.recipecompapp.data.database.dao.RecipeDao
import com.example.recipecompapp.data.database.entity.CategoryEntity
import com.example.recipecompapp.data.database.entity.RecipeEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipesDaoTest {
    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var recipeDao: RecipeDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        categoryDao = database.categoryDao()
        recipeDao = database.recipeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun createCategory(id: Int, name: String) = CategoryEntity(id, name, "", "")

    private fun createRecipe(id: Int, title: String, categoryId: String) =
        RecipeEntity(id, title, categoryId, "", "", "")

    @Test
    fun insertsAndRetrievesCategories() = runTest {
        val categories = listOf(
            CategoryEntity(
                id = 1,
                name = "Бургеры",
                description = "",
                imageUrl = "burgers.jpg"
            ),
            CategoryEntity(
                id = 2,
                name = "Паста",
                description = "",
                imageUrl = "pasta.jpg"
            )
        )

        categoryDao.insertCategory(categories)
        val retrieved = categoryDao.getCategories().first()

        assertTrue(retrieved.isNotEmpty())
        assertEquals(2, retrieved.size)
    }

    @Test
    fun insertReplacesDuplicateCategory() = runTest {
        categoryDao.insertCategory(
            listOf(
                createCategory(1, "Бургеры"),
                createCategory(1, "Паста")
            )
        )

        val retrieved = categoryDao.getCategories().first()

        assertEquals(1, retrieved.size)
        val saved = retrieved.first()
        assertEquals(1, saved.id)
        assertEquals("Паста", saved.name)
        assertEquals("", saved.description)
        assertEquals("", saved.imageUrl)
    }

    @Test
    fun getRecipesByCategoryReturnsCorrectItems() = runTest {
        val categories = listOf(
            createCategory(1, "Бургеры"),
            createCategory(2, "Десерты")
        )

        categoryDao.insertCategory(categories)

        val recipes = listOf(
            createRecipe(1, "Чизбургер", "1"),
            createRecipe(2, "Бургер с беконом", "1"),
            createRecipe(3, "Паннакота", "2")
        )
        recipeDao.insertRecipesList(recipes)
        val retrieved = recipeDao.getRecipesList("1").first()

        assertEquals(2, retrieved.size)
        retrieved.forEach { recipe ->
            assertEquals("1", recipe.categoryId)
        }
        assertTrue(retrieved.map { it.id }.containsAll(listOf(1, 2)))
    }

    @Test
    fun emptyDatabaseReturnsEmptyList() = runTest {
        val categories = categoryDao.getCategories().first()

        assertTrue(categories.isEmpty())
    }
}