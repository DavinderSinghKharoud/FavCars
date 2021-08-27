package com.example.favcars.application

import android.app.Application
import com.example.favcars.model.database.CarsRepository
import com.example.favcars.model.database.CarsRoomDatabase

class FavCarsApplication : Application() {

    private val database by lazy { CarsRoomDatabase.getDatabase(this@FavCarsApplication) }

    val repository by lazy { CarsRepository(database.carsDao()) }
}