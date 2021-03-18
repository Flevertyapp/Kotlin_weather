package ru.example.myapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.example.myapplication.R
import ru.example.myapplication.databinding.MainFragmentBinding
import ru.example.myapplication.model.Weather
import ru.example.myapplication.viewmodel.AppState
import ru.example.myapplication.viewmodel.MainViewModel

class MainFragment : Fragment() {
    //binding- замена findViewById для взаимодействия с вьюхами и их создания
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!! //здесь возвращает мейнфрагмент, !!= я уверен, что налла здесь не будет, компилятор отстань
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            MainFragmentBinding.inflate(inflater, container, false) //инициализация объекта, если
        //обращаться к binding раньше создания, приложение упадет
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) { //здесь создается вбю модель
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(MainViewModel::class.java) //вьюмодель привязана к жизненному циклу фрагмента через провайдер
        //получаем класс- MainViewModel::class.java = MainViewModel.class / синтаксис котлина
        viewModel.getLiveData()
            .observe(viewLifecycleOwner, //подписываемся на вью модель, учитываю в каком состоянии наш объект (viewLifecycleOwner)
                Observer { renderData(it) }) //наблюдатель здесь видит изменение данных
        viewModel.getWeatherFromLocalSource() //получаем данные
    }

    private fun renderData(appState: AppState) {
        when (appState) { //аналог ифа, данные отображаются в зависимости от состояния объекта
            is AppState.Success -> {
                val weatherData = appState.weatherData
                binding.loadingLayout.visibility = View.GONE
                setData(weatherData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE //включает мотылятор
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainView, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) { viewModel.getWeatherFromLocalSource() }
                    .show()
            }
        }

    }

    private fun setData(weatherData: Weather) {
        binding.cityName.text = weatherData.city.city
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherData.city.lat.toString(), weatherData.city.lon.toString(),
        )
        binding.feelsLikeValue.text =
            weatherData.feelsLike.toString()  //а корректно ль так тянуть? по умолчанию без этого не тянет
        binding.temperatureValue.text = weatherData.temperature.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null //очищаем binding
    }

    companion object {
        fun newInstance() = MainFragment()
    }

}