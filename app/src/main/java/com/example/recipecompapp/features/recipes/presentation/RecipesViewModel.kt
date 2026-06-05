package com.example.recipecompapp.features.recipes.presentation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecompapp.data.repository.RecipesRepository
import com.example.recipecompapp.features.recipes.presentation.model.RecipesUiState
import com.example.recipecompapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import kotlin.String

class RecipesViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository
) : ViewModel() {

    private val categoryId: Int = savedStateHandle["categoryId"] ?: 0
    private val stringTitle: String = savedStateHandle["categoryTitle"] ?: ""
    private val stringImageUrl: String = savedStateHandle["categoryImageUrl"] ?: ""

    private val categoryTitle: String = decodeString(stringTitle)
    private val categoryImageUrl: String = decodeString(stringImageUrl)

    private val _uiState = MutableStateFlow(
        RecipesUiState(
            categoryTitle = categoryTitle,
            categoryImageUrl = categoryImageUrl,
            isLoading = true
        )
    )
    val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

    init {
        loadRecipes()
    }

    private fun decodeString(string: String): String {
        return try {
            URLDecoder.decode(string, "UTF-8")
        } catch (e: Exception) {
            Uri.decode(string)
        }
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            repository.getRecipesByCategory(categoryId)
                .map { recipesDto -> recipesDto.map { it.toUiModel() } }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
                .collect { recipes ->
                    _uiState.update {
                        it.copy(
                            recipes = recipes,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
}