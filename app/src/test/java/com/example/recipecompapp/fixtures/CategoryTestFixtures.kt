package com.example.recipecompapp.fixtures

import com.example.recipecompapp.data.model.CategoryDto

object CategoryTestFixtures {
    fun createCategoryDto(
        id: Int = 1,
        title: String = "Бургеры",
        description: String = "Рецепты всех популярных видов бургеров",
        imageUrl: String = "burgers.jpg"
    ) = CategoryDto(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl
    )

    fun createCategoryDtoList(count: Int = 3) =
        List(count) { index -> createCategoryDto(id = index + 1, title = "Категория ${index + 1}") }
}