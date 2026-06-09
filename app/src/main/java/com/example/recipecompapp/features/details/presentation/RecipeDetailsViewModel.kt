package com.example.recipecompapp.features.details.presentation

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.recipecompapp.R
import com.example.recipecompapp.core.Constants.KEY_SERVINGS
import com.example.recipecompapp.data.local.datastore.FavoriteDataStoreManager
import com.example.recipecompapp.data.repository.RecipesRepository
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
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val resources: Resources,
    private val repository: RecipesRepository,
    private val dataStoreManager: FavoriteDataStoreManager,
) : AndroidViewModel(application) {
    private val recipeId = savedStateHandle["recipeId"] ?: -1
    private val _uiState = MutableStateFlow(RecipeDetailsUiState(isLoading = true))
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    init {
        loadRecipe()
        observeFavoriteStatus()
    }

    private fun loadRecipe() {
        viewModelScope.launch {
            repository.getRecipe(recipeId)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: resources.getString(R.string.unknown_error)
                        )
                    }
                }
                .collect { recipeDto ->
                    _uiState.update { currentState ->
                        if (recipeDto == null) {
                            currentState.copy(isLoading = false, error = resources.getString(R.string.not_available_offline))
                        } else {
                            val recipeUi = recipeDto.toUiModel()
                            val restoredPortions = savedStateHandle.get<Int>(KEY_SERVINGS) ?: DEFAULT_SERVINGS
                            val newState = currentState.copy(
                                recipe = recipeUi,
                                currentPortions = restoredPortions,
                                isLoading = false,
                                error = null
                            )
                           val scaled = newState.recalcIngredients()
                            newState.copy(scaledIngredients = scaled)
                        }
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
