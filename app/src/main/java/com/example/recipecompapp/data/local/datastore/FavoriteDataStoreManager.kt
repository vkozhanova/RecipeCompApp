package com.example.recipecompapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FavoriteDataStoreManager(
    private val context: Context
) {
    suspend fun isFavorite(recipeId: Int): Boolean {
        return try {
            val preferences = context.dataStore.data.first()
            val favoriteIds = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] ?: emptySet()
            favoriteIds.contains(recipeId.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun addFavorite(recipeId: Int) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] ?: emptySet()
            val updateFavorites = currentFavorites + recipeId.toString()
            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] = updateFavorites
        }
    }

    suspend fun removeFavorite(recipeId: Int) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] ?: emptySet()
            val updateFavorites = currentFavorites - recipeId.toString()
            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] = updateFavorites
        }
    }

    suspend fun toggleFavorite(recipeId: Int): Boolean {
        val idString = recipeId.toString()
        val wasFavorite = isFavorite(recipeId)
        context.dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] ?: emptySet()
            val newSet = if (wasFavorite) current - idString else current + idString
            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] = newSet
        }
        return !wasFavorite
    }

    fun getFavoriteIdsFlow(): Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] ?: emptySet()
        }

    fun isFavoriteFlow(recipeId: Int): Flow<Boolean> = getFavoriteIdsFlow()
        .map { favoriteIds ->
            favoriteIds.contains(recipeId.toString())
        }

    fun getFavoriteCountFlow(): Flow<Int> = getFavoriteIdsFlow()
        .map { it.size }
}