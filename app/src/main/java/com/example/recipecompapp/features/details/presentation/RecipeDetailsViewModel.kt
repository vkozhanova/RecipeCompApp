package com.example.recipecompapp.features.details.presentation

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecompapp.R
import com.example.recipecompapp.core.Constants.KEY_SERVINGS
import com.example.recipecompapp.data.local.datastore.FavoriteDataStoreManager
import com.example.recipecompapp.data.repository.RecipesRepositoryStub
import com.example.recipecompapp.features.details.presentation.model.DEFAULT_SERVINGS
import com.example.recipecompapp.features.details.presentation.model.RecipeDetailsUiState
import com.example.recipecompapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val resources: Resources,
    private val  repository: RecipesRepositoryStub,
    private val dataStoreManager: FavoriteDataStoreManager,
) : ViewModel() {
    private val recipeId = savedStateHandle["recipeId"] ?: -1
    private val _uiState = MutableStateFlow(RecipeDetailsUiState(isLoading = true))
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    init {
        loadRecipe(recipeId)
        observeFavoriteStatus()
    }

    private fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val recipeDto = repository.getRecipeById(recipeId)
                if (recipeDto != null) {
                    val recipeUi = recipeDto.toUiModel()
                    val restoredPortions =
                        savedStateHandle.get<Int>(KEY_SERVINGS) ?: DEFAULT_SERVINGS
                    val newSate = RecipeDetailsUiState(
                        recipe = recipeUi,
                        currentPortions = restoredPortions,
                        isLoading = false,
                        isFavorite = _uiState.value.isFavorite,
                        error = null,
                        scaledIngredients = emptyList()
                    )
                    val scaled = newSate.recalcIngredients()
                    _uiState.update { newSate.copy(scaledIngredients = scaled) }
                } else {
                    _uiState.update {
                        it.copy(
                            error = resources.getString(R.string.recipe_not_found),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: resources.getString(R.string.download_error),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            dataStoreManager.isFavoriteFlow(recipeId)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { isFavorite ->
                    _uiState.update { it.copy(isFavorite = isFavorite) }
                }
        }
    }

    fun updatePortions(portions: Int) {
        val currentState = _uiState.value
        val newState = currentState.copy(currentPortions = portions)
        val newScaled = newState.recalcIngredients()
        _uiState.update { newState.copy(scaledIngredients = newScaled) }
        savedStateHandle[KEY_SERVINGS] = portions
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            dataStoreManager.toggleFavorite(recipeId)
        }
    }
}
