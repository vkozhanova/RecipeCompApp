package com.example.recipecompapp.data.repository

import app.cash.turbine.test
import com.example.recipecompapp.data.database.RecipesDatabase
import com.example.recipecompapp.data.database.dao.CategoryDao
import com.example.recipecompapp.data.database.dao.RecipeDao
import com.example.recipecompapp.data.model.toEntity
import com.example.recipecompapp.data.network.api.RecipesApiService
import com.example.recipecompapp.fixtures.CategoryTestFixtures
import com.example.recipecompapp.fixtures.RecipeTestFixtures
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class RecipesRepositoryTest {
    private val api = mockk<RecipesApiService>()
    private val database = mockk<RecipesDatabase>(relaxed = true)
    private val categoryDao = mockk<CategoryDao>()
    private val recipeDao = mockk<RecipeDao>()

    private lateinit var repository: RecipesRepositoryImpl

    @Before
    fun setUp() {
        every { database.categoryDao() } returns categoryDao
        every { database.recipeDao() } returns recipeDao
        repository = RecipesRepositoryImpl(api, database)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getCategories emits categories from database`() = runTest {
        val categoryDto = CategoryTestFixtures.createCategoryDto(
            id = 1,
            title = "Бургеры",
            description = "Рецепты всех популярных видов бургеров",
            imageUrl = "burgers.jpg"
        )
        val categoryEntity = categoryDto.toEntity()
        every { categoryDao.getCategories() } returns flowOf(listOf(categoryEntity))

        coEvery { api.getCategories() } returns emptyList()
        coEvery { categoryDao.insertCategory(any()) } just Runs

        repository.getCategories().test {
            val categories = awaitItem()
            assertEquals(1, categories.size)
            assertEquals("Бургеры", categories[0].title)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1, timeout = VERIFY_TIMEOUT) { api.getCategories() }
        coVerify(exactly = 1, timeout = VERIFY_TIMEOUT) { categoryDao.insertCategory(any()) }
    }

    @Test
    fun `getCategories still emits data when api throws exception`() = runTest {
        val categoryDto = CategoryTestFixtures.createCategoryDto(
            id = 1,
            title = "Бургеры",
            description = "Рецепты всех популярных видов бургеров",
            imageUrl = "burgers.jpg"
        )
        val categoryEntity = categoryDto.toEntity()

        every { categoryDao.getCategories() } returns flowOf(listOf(categoryEntity))
        coEvery { api.getCategories() } throws Exception("Network error")

        repository.getCategories().test {
            val categories = awaitItem()
            assertEquals(1, categories.size)
            assertEquals("Бургеры", categories[0].title)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1, timeout = VERIFY_TIMEOUT) { api.getCategories() }
        coVerify(exactly = 0, timeout = VERIFY_TIMEOUT) { categoryDao.insertCategory(any()) }
    }

    @Test
    fun `getRecipesByCategory returns flow filtered by categoryId`() = runTest {
        val categoryId1 = 1
        val categoryId2 = 2

        val recipeDto1 = RecipeTestFixtures.createRecipeDto(
            id = 1,
            title = "Паста Карбонара",
            ingredients = listOf(
                RecipeTestFixtures.createIngredientDto(
                    quantity = "200",
                    unitOfMeasure = "г",
                    description = "Спагетти"
                )
            ),
            method = listOf("Отварить", "Смешать"),
            imageUrl = "carbonara.jpg"
        )
        val recipeEntity1 = recipeDto1.toEntity(categoryId1)

        val recipeDto2 = RecipeTestFixtures.createRecipeDto(
            id = 2,
            title = "Паста Болоньезе",
            ingredients = listOf(
                RecipeTestFixtures.createIngredientDto(
                    quantity = "200",
                    unitOfMeasure = "г",
                    description = "Спагетти"
                )
            ),
            method = listOf("Отварить", "Смешать"),
            imageUrl = "bolognese.jpg"
        )
        val recipeEntity2 = recipeDto2.toEntity(categoryId1)

        val recipeDto3 = RecipeTestFixtures.createRecipeDto(
            id = 3,
            title = "Бургер",
            ingredients = listOf(
                RecipeTestFixtures.createIngredientDto(
                    quantity = "1",
                    unitOfMeasure = "шт",
                    description = "Булка"
                )
            ),
            method = listOf("Собрать", "Поджарить"),
            imageUrl = "burger.jpg"
        )
        val recipeEntity3 = recipeDto3.toEntity(categoryId2)


        every { recipeDao.getRecipesList(categoryId1.toString()) } returns flowOf(
            listOf(
                recipeEntity1,
                recipeEntity2
            )
        )
        every { recipeDao.getRecipesList(categoryId2.toString()) } returns flowOf(
            listOf(
                recipeEntity3
            )
        )

        coEvery { api.getRecipesByCategory(any()) } returns emptyList()
        coEvery { recipeDao.insertRecipesList(any()) } just Runs

        repository.getRecipesByCategory(categoryId1).test {
            val recipes = awaitItem()
            assertEquals(2, recipes.size)
            assertEquals("Паста Карбонара", recipes[0].title)
            assertEquals("Паста Болоньезе", recipes[1].title)
            cancelAndIgnoreRemainingEvents()
        }

        repository.getRecipesByCategory(categoryId2).test {
            val recipes = awaitItem()
            assertEquals(1, recipes.size)
            assertEquals("Бургер", recipes[0].title)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1, timeout = VERIFY_TIMEOUT) { api.getRecipesByCategory(categoryId1) }
        coVerify(exactly = 1, timeout = VERIFY_TIMEOUT) { api.getRecipesByCategory(categoryId2) }
        coVerify(exactly = 2, timeout = VERIFY_TIMEOUT) { recipeDao.insertRecipesList(any()) }
    }
}

private const val VERIFY_TIMEOUT = 1000L