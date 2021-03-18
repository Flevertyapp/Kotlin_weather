package ru.example.myapplication.model

//здесь погода локаль
data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = -15,
    val feelsLike: Int = +27
)

//город по умолчанию
fun getDefaultCity() = City("Москва", 55.755826, 37.617299900000035)



