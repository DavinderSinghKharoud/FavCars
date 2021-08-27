package com.example.favcars.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.favcars.model.entities.Car

@Database(entities = [Car::class], version = 1, exportSchema = false)
abstract class CarsRoomDatabase : RoomDatabase() {

    abstract fun carsDao(): CarsDao

    companion object {
        @Volatile
        private var INSTANCE: CarsRoomDatabase? = null

        fun getDatabase(context: Context): CarsRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CarsRoomDatabase::class.java,
                    "cars_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}