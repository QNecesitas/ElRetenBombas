package com.qnecesitas.elretenbombas.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Query("SELECT * FROM client ORDER BY year DESC,month DESC,day DESC")
    fun fetchClients(): Flow<List<Client>>

    @Query("SELECT * FROM client WHERE day=:day AND month=:month AND year=:year ORDER BY year DESC,month DESC,day DESC")
    fun fetchClientsDay(day: Int, month: Int, year: Int): Flow<List<Client>>

    @Query("SELECT * FROM client WHERE month=:month AND year=:year ORDER BY year DESC,month DESC,day DESC")
    fun fetchClientsMonth(month: Int, year: Int): Flow<List<Client>>

    @Query("SELECT * FROM client WHERE year=:year ORDER BY year DESC,month DESC,day DESC")
    fun fetchClientsYear(year: Int): Flow<List<Client>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClient(client: Client)

    @Update
    suspend fun updateClient(client: Client)

    @Delete
    suspend fun deleteClient(client: Client)
}