package com.example.recipecompapp.di

interface Factory<T> {
    fun create(): T
}