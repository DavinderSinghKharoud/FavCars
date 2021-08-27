package com.example.favcars.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.favcars.model.entities.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCarDetails(car: Car)

    @Query("SELECT * FROM cars_table ORDER BY ID")
    fun getAllCarsList(): Flow<List<Car>>
}
