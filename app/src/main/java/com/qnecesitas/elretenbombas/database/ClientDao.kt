package com.qnecesitas.elretenbombas.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Query("SELECT * FROM client ORDER BY name")
    fun fetchClients(): Flow<List<Client>>

    @Query("INSERT INTO client VALUES ('id','name','day','month','year','phone','waterBomb','copli','imperente','price','warranty','descWork','descClient')")
    fun addClient()

}