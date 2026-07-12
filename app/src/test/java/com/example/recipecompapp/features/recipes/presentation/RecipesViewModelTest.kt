package com.example.recipecompapp.features.recipes.presentation

import java.io.IOException
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.recipecompapp.data.repository.RecipesRepository
import com.example.recipecompapp.fixtures.RecipeTestFixtures
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipesViewModelTest {
    private lateinit var repository: RecipesRepository
    private lateinit var viewModel: RecipesViewModel

    private fun createViewModel(
        categoryId: Int,
        categoryTitle: String = "",
        categoryImageUrl: String = "",
    ): RecipesViewModel {
        val handle = SavedStateHandle(
            mapOf(
                "categoryId" to categoryId,
                "categoryTitle" to categoryTitle,
                "categoryImageUrl" to categoryImageUrl
            )
        )
        return RecipesViewModel(handle, repository)
    }


    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = mockk()
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `loads recipes for category`() = runTest {
        val recipes = RecipeTestFixtures.createRecipeDtoList(3)
        every { repository.getRecipesByCategory(1) } returns flowOf(recipes)

        viewModel = createViewModel(
            categoryId = 1,
            categoryTitle = "Бургеры",
            categoryImageUrl = "burgers.jpg"
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(3, state.recipes.size)
            assertFalse(state.isLoading)
            assertNull(state.error)
            assertEquals("Бургеры", state.categoryTitle)
            assertEquals("Рецепт 1", state.recipes[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state reflects category title from savedState`() = runTest {
        every { repository.getRecipesByCategory(1) } returns flowOf(emptyList())

        viewModel = createViewModel(
            categoryId = 1,
            categoryTitle = "Завтраки",
            categoryImageUrl = ""
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Завтраки", state.categoryTitle)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows error when repository throws`() = runTest {
        every { repository.getRecipesByCategory(1) } returns flow {
            throw IOException("Network error")
        }

        viewModel = createViewModel(
            categoryId = 1,
            categoryTitle = "Бургеры",
            categoryImageUrl = "burgers.jpg"
        )

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNotNull(state.error)
            assertTrue(state.recipes.isEmpty())
            assertEquals("Network error", state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }
}