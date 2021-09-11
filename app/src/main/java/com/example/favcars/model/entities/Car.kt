package com.example.favcars.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cars_table")
data class Car(
    @ColumnInfo val image: String,
    @ColumnInfo(name = "image_source") val imageSource: String,
    @ColumnInfo val name: String,
    @ColumnInfo val type: String,
    @ColumnInfo(name = "engine_power") val enginePow: String,
    @ColumnInfo val description: String,
    @ColumnInfo(name = "price_range") val priceRange: String,
    @ColumnInfo(name = "review") val review: String,
    @ColumnInfo(name = "favorite_car") val favCar: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
): Parcelable
