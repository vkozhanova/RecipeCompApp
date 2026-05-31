package com.example.recipecompapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipecompapp.data.database.dao.CategoryDao
import com.example.recipecompapp.data.database.entity.CategoryEntity
import kotlin.jvm.java

@Database(
    entities = [CategoryEntity::class],
    version = 1,
    exportSchema = false
)

abstract class RecipesDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
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