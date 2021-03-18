package ru.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.example.myapplication.model.Repository
import ru.example.myapplication.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repository = RepositoryImpl()
) : ViewModel() {
    fun getLiveData(): LiveData<AppState> = liveDataToObserve //подписываемся на получение данных
    fun getWeatherFromLocalSource() = getDataFromLocalSource() //здесь возвращается юнит
    fun getWeatherFromRemoteSource() = getDataFromLocalSource()
    private fun getDataFromLocalSource() { //симуляция сети
        liveDataToObserve.value = AppState.Loading //кладем данные в лайвдату/ типа грузится
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(//postValue для синхронизации с мейн потоком, иначе пытается обратиться к вью не из мейн потока
                AppState.Success(repositoryImpl.getWeatherFromLocalStorage())
            ) //обновляем данные в лайвдате
        }.start()
    }
}