package com.example.favcars.view_model

import androidx.lifecycle.*
import com.example.favcars.model.database.CarsRepository
import com.example.favcars.model.entities.Car
import kotlinx.coroutines.launch

class CarsViewModel(private val repository: CarsRepository) : ViewModel() {

    fun insert(car: Car) = viewModelScope.launch {
        repository.insertCar(car)
    }

    val allCarsList: LiveData<List<Car>> = repository.allCarsList.asLiveData()
}

class CarsViewModelFactory(private val repository: CarsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}