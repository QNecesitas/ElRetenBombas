package com.qnecesitas.elretenbombas.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Client(
    @PrimaryKey var c_client: Int,
    @NonNull @ColumnInfo(name = "name") var name: String,
    @NonNull @ColumnInfo(name = "day") var day: Int,
    @NonNull @ColumnInfo(name = "month") var month: Int,
    @NonNull @ColumnInfo(name = "year") var year: Int,
    @NonNull @ColumnInfo(name = "phone") var phone: String,
    @NonNull @ColumnInfo(name = "waterBomb") var waterBomb: String,
    @NonNull @ColumnInfo(name = "copli") var copli: String,
    @NonNull @ColumnInfo(name = "imperente") var imperente: String,
    @NonNull @ColumnInfo(name = "price") var price: Double,
    @NonNull @ColumnInfo(name = "warranty") var warranty: Int,
    @NonNull @ColumnInfo(name = "descWork") var descWork: String,
    @NonNull @ColumnInfo(name = "descClient") var descClient: String
)