# RecipeCompApp

[![Android CI](https://github.com/vkozhanova/RecipeCompApp/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/vkozhanova/RecipeCompApp/actions/workflows/ci.yml)
Android-приложение для просмотра рецептов, разработанное с использованием Jetpack Compose и современных компонентов Android Jetpack.
## Возможности
- Просмотр списка рецептов
- Просмотр рецептов по категориям
- Детальная информация о рецепте
- Добавление и удаление избранных рецептов
- Локальное сохранение данных
- Работа с удалённым API
## Demo
<table>
<tr>
<td align="center"><b>Categories</b></td>
<td align="center"><b>Details</b></td>
</tr>
<tr>
<td><img src="screenshots/gif/cat.gif" width="345" alt=""></td>
<td><img src="screenshots/gif/det.gif" width="344" alt=""></td>
</tr>
</table>

## Реализовано
- современный UI на Jetpack Compose;
- архитектура MVVM;
- внедрение зависимостей через Hilt;
- локальное хранение данных в Room;
- хранение пользовательских настроек в DataStore;
- получение данных по сети через Retrofit;
- навигация Navigation Compose;
- автоматическая сборка через GitHub Actions;
- автоматические тесты и отчёты покрытия кода.
## Технологии
- Kotlin
- Jetpack Compose + Material 3
- MVVM
- Hilt
- Navigation Compose
- Retrofit
- Room
- DataStore
- Coil
- Coroutines + Flow
- Kotlinx Serialization
## Архитектура
Проект построен по архитектуре MVVM с разделением ответственности между слоями UI, ViewModel и Repository.
```
UI (Compose)
      │
ViewModel
      │
Repository
      │
────────────────────────────
Remote API (Retrofit)
Local Database (Room)
DataStore
```
Для упрощения поддержки и масштабирования, каждая функциональность вынесена в отдельный feature-модуль по пакетам:
```
features/
    categories/
    recipes/
    details/
    favorites/
```
## Структура проекта
```
app
├── core
│   ├── navigation
│   └── ui
│       └── screenheader
├── data
│   ├── database
│   │   ├── converter
│   │   ├── dao
│   │   └── entity 
│   ├── local
│   │   ├── datastore
│   │   └── preferences
│   ├── model
│   ├── network
│   │   └── api
│   └── repository
├── di
├── features
│   ├── bottom
│   │   ├── presentation
│   │   └── ui
│   ├── categories
│   │   ├── presentation
│   │   │   └── model
│   │   └── ui
│   ├── details
│   │   ├── presentation
│   │   │   └── model
│   │   └── ui
│   ├── favorites
│   │   ├── presentation
│   │   │   └── model
│   │   └── ui
│   ├── recipes
│   │   ├── presentation
│   │   │   └── model
│   │   └── ui
└── ui
    └── theme
```
## Тестирование
Покрытие кода собирается с использованием **JaCoCo**.
###  В проекте реализованы:
- Unit-тесты
- Instrumentation-тесты
- Интеграционные тесты
- UI-тесты Jetpack Compose
- End-to-End тесты
- Hilt Test
- Kaspresso
## CI
Для проекта настроен GitHub Actions.
При каждом push и pull request автоматически выполняются:
- сборка проекта;
- запуск тестов;
- проверка успешности сборки.
## Запуск проекта
1. Клонируйте репозиторий:
```bash
git clone https://github.com/vkozhanova/RecipeCompApp.git
```
2. Откройте проект в Android Studio.
3. Дождитесь синхронизации Gradle.
4. Запустите приложение на эмуляторе или физическом устройстве.
---
## Автор
**Vera Kozhanova**

GitHub: [@vkozhanova](https://github.com/vkozhanova)