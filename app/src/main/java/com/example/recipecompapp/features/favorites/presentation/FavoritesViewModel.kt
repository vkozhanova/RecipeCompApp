package com.example.recipecompapp.features.favorites.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
    application: Application,
) : AndroidViewModel(application) {
    private val dataStoreManager = FavoriteDataStoreManager(application)
    private val repository = RecipesRepositoryStub
    private val _uiState = MutableStateFlow(FavoritesUiState(isLoading = true))
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val context = getApplication<Application>()

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
                            error = e.message ?: context.getString(R.string.favorites_error)
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
}
