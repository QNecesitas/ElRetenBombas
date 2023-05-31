package com.qnecesitas.elretenbombas.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Client(
    @PrimaryKey(autoGenerate = true) var c_client: Int,
    @ColumnInfo(name = "ci") var ci: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "day") var day: Int,
    @ColumnInfo(name = "month") var month: Int,
    @ColumnInfo(name = "year") var year: Int,
    @ColumnInfo(name = "phone") var phone: String,
    @ColumnInfo(name = "waterBomb") var waterBomb: String,
    @ColumnInfo(name = "copli") var copli: String,
    @ColumnInfo(name = "imperente") var imperente: String,
    @ColumnInfo(name = "price1") var price1: Double,
    @ColumnInfo(name = "typePrice1") var typePrice1: String,
    @ColumnInfo(name = "price2") var price2: Double,
    @ColumnInfo(name = "typePrice2") var typePrice2: String,
    @ColumnInfo(name = "warranty") var warranty: Int,
    @ColumnInfo(name = "descWork") var descWork: String,
    @ColumnInfo(name = "descClient") var descClient: String
)