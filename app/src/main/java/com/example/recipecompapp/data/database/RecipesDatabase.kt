package com.example.recipecompapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipecompapp.data.database.dao.CategoryDao
import com.example.recipecompapp.data.database.dao.RecipeDao
import com.example.recipecompapp.data.database.entity.CategoryEntity
import com.example.recipecompapp.data.database.entity.RecipeEntity
import kotlin.jvm.java

@Database(
    entities = [CategoryEntity::class, RecipeEntity::class],
    version = 1,
    exportSchema = false
)

abstract class RecipesDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun recipeDao(): RecipeDao
    companion object {
        @Volatile
        private var INSTANCE: RecipesDatabase? = null

        fun buildDatabase(context: Context): RecipesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipesDatabase::class.java,
                    "recipes_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}