package com.example.recipecompapp.data.util

import android.content.Context
import androidx.core.content.edit

class FavoritePrefsManager(context: Context) {
    private val prefs = context.getSharedPreferences("recipe_app_prefs", Context.MODE_PRIVATE)
    private val FAVORITE_IDS_KEY = "favorite_recipe_ids"

    fun getAllFavorites(): Set<String> =
        prefs.getStringSet(FAVORITE_IDS_KEY, emptySet()) ?: emptySet()

    fun isFavorite(recipeId: Int): Boolean {
        val favorites = getAllFavorites()
        return favorites.contains(recipeId.toString())
    }

    fun addToFavorites(recipeId: Int) {
        val currentSet = getAllFavorites().toMutableSet()
        currentSet.add(recipeId.toString())
        prefs.edit { putStringSet(FAVORITE_IDS_KEY, currentSet) }
    }

    fun removeFromFavorites(recipeId: Int) {
        val currentSet = getAllFavorites().toMutableSet()
        currentSet.remove(recipeId.toString())
        prefs.edit { putStringSet(FAVORITE_IDS_KEY, currentSet) }
    }

    fun toggleFavorites(recipeId: Int): Boolean {
        return if (isFavorite(recipeId)) {
            removeFromFavorites(recipeId)
            false
        } else {
            addToFavorites(recipeId)
            true
        }
    }
}