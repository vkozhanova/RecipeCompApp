package com.example.recipecompapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesDataStoreTest {
    private lateinit var context: Context
    private lateinit var manager: FavoriteDataStoreManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        manager = FavoriteDataStoreManager(context)
    }

    @After
    fun tearDown() {
        runBlocking { context.dataStore.edit { it.clear() } }
    }

    @Test
    fun addFavoriteSavesRecipeId() = runTest {
        manager.addFavorite(42)
        val ids = manager.getFavoriteIdsFlow().first()
        assertTrue(manager.isFavorite(42))
        assertTrue(ids.contains("42"))
    }

    @Test
    fun removeFromFavoritesDeletesRecipeId() = runTest {
        manager.addFavorite(2)
        assertTrue(manager.isFavorite(2))
        assertEquals(setOf("2"), manager.getFavoriteIdsFlow().first())

        manager.removeFavorite(2)
        assertFalse(manager.isFavorite(2))
        assertEquals(emptySet<String>(), manager.getFavoriteIdsFlow().first())
    }

    @Test
    fun favoritesFlowEmitsUpdatesReactively() = runTest {
        manager.getFavoriteIdsFlow().test {
            val initial = awaitItem()
            assertTrue(initial.isEmpty())

            manager.addFavorite(1)

            val updated = awaitItem()
            assertEquals(setOf("1"), updated)

            manager.removeFavorite(1)
            val afterRemove = awaitItem()
            assertFalse(afterRemove.contains("1"))
            cancelAndIgnoreRemainingEvents()
        }
    }
}