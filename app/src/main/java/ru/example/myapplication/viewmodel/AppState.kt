package ru.example.myapplication.viewmodel

import ru.example.myapplication.model.Weather

//enum на стероидах)))
sealed class AppState { //sealed позволяет вернуть несколько  вариаций состояний объекта
    data class Success(val weatherData: Weather) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
//все состояния описываются только здесь, абстрактный класс