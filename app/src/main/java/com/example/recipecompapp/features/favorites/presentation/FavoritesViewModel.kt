package com.example.recipecompapp.features.favorites.presentation

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.recipecompapp.R
import com.example.recipecompapp.data.local.datastore.FavoriteDataStoreManager
import com.example.recipecompapp.data.repository.RecipesRepository
import com.example.recipecompapp.features.favorites.presentation.model.FavoritesUiState
import com.example.recipecompapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val resources: Resources,
    private val repository: RecipesRepository,
    private val dataStoreManager: FavoriteDataStoreManager,
) : AndroidViewModel(application) {
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
                .collect { ids ->
                    if (ids.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                recipes = emptyList(),
                                isLoading = false,
                                error = null
                            )
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = true) }
                        try {
                            val recipeIds = ids.mapNotNull { it.toIntOrNull() }
                            val recipes = repository.getRecipesByIds(recipeIds)
                            val uiModels = recipes.map { it.toUiModel() }
                            _uiState.update {
                                it.copy(
                                    recipes = uiModels,
                                    isLoading = false,
                                    error = null
                                )
                            }
                        } catch (e: Exception) {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = e.message ?: resources.getString(R.string.favorites_error)
                                )
                            }
                        }
                    }
                }
        }
    }
}
