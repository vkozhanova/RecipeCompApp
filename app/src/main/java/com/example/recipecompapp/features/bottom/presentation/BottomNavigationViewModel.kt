package com.example.recipecompapp.features.bottom.presentation

import androidx.lifecycle.ViewModel
import com.example.recipecompapp.data.local.datastore.FavoriteDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class BottomNavigationViewModel @Inject constructor(
    private val dataStoreManager: FavoriteDataStoreManager
) : ViewModel() {
    val favoriteCountFlow: Flow<Int> = dataStoreManager.getFavoriteCountFlow()
}