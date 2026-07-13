package com.example.recipecompapp.features.categories.presentation

import app.cash.turbine.test
import com.example.recipecompapp.data.repository.RecipesRepository
import com.example.recipecompapp.fixtures.CategoryTestFixtures
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
import java.io.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesViewModelTest {
    private lateinit var repository: RecipesRepository
    private lateinit var viewModel: CategoriesViewModel


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
    fun `loads categories from repository`() = runTest {
        val categories = CategoryTestFixtures.createCategoryDtoList(3)
        every { repository.getCategories() } returns flowOf(categories)

        viewModel = CategoriesViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(3, state.categories.size)
            assertEquals("Категория 1", state.categories[0].title)
            assertFalse(state.isLoading)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows empty list when repository returns no data`() = runTest {
        every { repository.getCategories() } returns flowOf(emptyList())

        viewModel = CategoriesViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.categories.isEmpty())
            assertFalse(state.isLoading)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `shows error when repository throws`() = runTest {
        every { repository.getCategories() } returns flow {
            throw IOException("Network error")
        }

        viewModel = CategoriesViewModel(repository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.categories.isEmpty())
            assertNotNull(state.error)
            assertEquals("Network error", state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }
}