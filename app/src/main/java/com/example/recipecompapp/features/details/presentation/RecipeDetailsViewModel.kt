package com.example.recipecompapp.features.details.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.recipecompapp.R
import com.example.recipecompapp.data.local.datastore.FavoriteDataStoreManager
import com.example.recipecompapp.data.repository.RecipesRepositoryStub
import com.example.recipecompapp.features.details.presentation.model.RecipeDetailsUiState
import com.example.recipecompapp.ui.recipes.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(
    application: Application,
    private val saveStateHandle: SavedStateHandle,
    ) : AndroidViewModel(application) {
    private val dataStoreManager = FavoriteDataStoreManager(getApplication())
    private val repository = RecipesRepositoryStub
    private val recipeId = saveStateHandle["recipeId"] ?: -1

    private val _uiState = MutableStateFlow(RecipeDetailsUiState(isLoading = true))
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    val context = getApplication<Application>()

    init{
      loadRecipe()
      observeFavoriteStatus()
    }

    private fun loadRecipe() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val  recipeDto = repository.getRecipeById(recipeId)
                if(recipeDto != null) {
                    val recipeUi = recipeDto.toUiModel()
                    _uiState.update {it.copy(recipe = recipeUi, isLoading = false)}
                } else {
                    _uiState.update { it.copy(error = context.getString(R.string.recipe_not_found), isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: context.getString(R.string.download_error), isLoading = false) }
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
        _uiState.update { it.copy(servings = portions) }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            dataStoreManager.toggleFavorite(recipeId)
        }
    }
}
