package com.example.favcars.model.database

import androidx.annotation.WorkerThread
import com.example.favcars.model.entities.Car
import kotlinx.coroutines.flow.Flow


class CarsRepository(private val carsDao: CarsDao) {

    @WorkerThread
    suspend fun insertCar(car: Car) {
        carsDao.insertCarDetails(car)
    }

    val allCarsList: Flow<List<Car>> = carsDao.getAllCarsList()
}