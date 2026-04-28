package com.example.recipecompapp.features.recipes.presentation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecompapp.data.repository.RecipesRepositoryStub
import com.example.recipecompapp.features.recipes.presentation.model.RecipesUiState
import com.example.recipecompapp.ui.recipes.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import kotlin.String

class RecipesViewModel(
    private val savedStateHandle: SavedStateHandle
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
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val recipesDto = RecipesRepositoryStub.getRecipesByCategoryId(categoryId)
                val recipes = recipesDto.map { it.toUiModel() }

                _uiState.update {
                    it.copy(
                        recipes = recipes,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Неизвестная ошибка при загрузке рецептов"
                    )
                }
            }
        }
    }
}