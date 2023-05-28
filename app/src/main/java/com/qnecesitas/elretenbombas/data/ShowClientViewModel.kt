package com.qnecesitas.elretenbombas.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qnecesitas.elretenbombas.database.Client
import com.qnecesitas.elretenbombas.database.ClientDao
import kotlinx.coroutines.flow.Flow

class ShowClientViewModel( private val clientDao: ClientDao): ViewModel() {

    //val alClient: LiveData<ArrayList<Client>> =
    fun getAllClients(): Flow<List<Client>> = clientDao.fetchClients()

}

class ClientViewModelFactory (
    private val clientDao: ClientDao
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ShowClientViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return  ShowClientViewModel(clientDao) as T
        }
        throw IllegalArgumentException("Unknow viewModel class")
    }
}