package com.example.favcars.model.database

import androidx.room.*
import com.example.favcars.model.entities.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCarDetails(car: Car)

    @Query("SELECT * FROM cars_table ORDER BY ID")
    fun getAllCarsList(): Flow<List<Car>>

    @Update
    suspend fun updateFavCarDetails(car: Car)

    @Query("SELECT * FROM cars_table WHERE favorite_car = 1")
    fun getFavoriteCarsList(): Flow<List<Car>>
}
