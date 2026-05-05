package com.example.recipecompapp.features.favorites.presentation

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecompapp.R
import com.example.recipecompapp.data.local.datastore.FavoriteDataStoreManager
import com.example.recipecompapp.data.repository.RecipesRepositoryStub
import com.example.recipecompapp.features.favorites.presentation.model.FavoritesUiState
import com.example.recipecompapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val resources: Resources,
    private val repository: RecipesRepositoryStub,
    private val dataStoreManager: FavoriteDataStoreManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesUiState(isLoading = true))
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            dataStoreManager.getFavoriteIdsFlow()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: resources.getString(R.string.favorites_error)
                        )
                    }
                }
                .map { ids ->
                    ids.mapNotNull { id ->
                        repository.getRecipeById(id.toIntOrNull() ?: -1)?.toUiModel()
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

    fun toggleFavorite(recipeId: Int) {
        viewModelScope.launch {
            dataStoreManager.toggleFavorite(recipeId)
        }
    }
}
