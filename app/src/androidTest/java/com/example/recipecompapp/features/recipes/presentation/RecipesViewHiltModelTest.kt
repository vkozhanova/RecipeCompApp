package com.example.recipecompapp.features.recipes.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.recipecompapp.data.repository.RecipesRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class RecipesViewHiltModelTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var repository: RecipesRepository

    private lateinit var viewModel: RecipesViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(
            testDispatcher
        )
        hiltRule.inject()

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "categoryId" to 0,
                "categoryTitle" to "Бургеры",
                "categoryImageUrl" to "burger.png"
            )
        )
        viewModel = RecipesViewModel(savedStateHandle, repository)
    }

    @Test
    fun testRecipesLoadingSuccessfully() = runTest(testDispatcher) {
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertEquals(1, state.recipes.size)
        assertEquals("Классический бургер с говядиной", state.recipes[0].title)

    }
}