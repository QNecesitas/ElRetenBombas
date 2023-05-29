package com.qnecesitas.elretenbombas.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qnecesitas.elretenbombas.database.Client
import com.qnecesitas.elretenbombas.database.ClientDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

class ShowClientViewModel( private val clientDao: ClientDao): ViewModel() {

    private val _alClients = MutableLiveData<Flow<List<Client>>>()
    val alClients: LiveData<Flow<List<Client>>> get() = _alClients


    fun getAllClients() {
        _alClients.postValue(clientDao.fetchClients())
    }

    fun filterByText(text: String){

    }

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