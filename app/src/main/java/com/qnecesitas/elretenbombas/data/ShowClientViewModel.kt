package com.qnecesitas.elretenbombas.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.qnecesitas.elretenbombas.auxiliary.IDCreater
import com.qnecesitas.elretenbombas.database.Client
import com.qnecesitas.elretenbombas.database.ClientDao
import kotlinx.coroutines.launch
import java.time.Year

class ShowClientViewModel(private val clientDao: ClientDao) : ViewModel() {

    /*Live Data
    ---------
     */
    private val _alClients = MutableLiveData<List<Client>>()
    val alClients: LiveData<List<Client>> get() = _alClients

    private val _alClientsFilter = MutableLiveData<List<Client>>()
    val alClientsFilter: LiveData<List<Client>> get() = _alClientsFilter

    private val _day = MutableLiveData<Int>()
    val day: LiveData<Int> get() = _day

    private val _month = MutableLiveData<Int>()
    val month: LiveData<Int> get() = _month

    private val _year = MutableLiveData<Int>()
    val year: LiveData<Int> get() = _year


    /*Save date
    ---------
     */
    fun saveLastDay(day: Int) {
        _day.value = day
    }

    fun saveLastMonth(month: Int) {
        _month.value = month
    }

    fun saveLastYear(year: Int) {
        _year.value = year
    }


    /*Fetch Info
    ---------
     */
    suspend fun getAllClients() {
        clientDao.fetchClients().collect() {
            _alClients.postValue(it)
            _alClientsFilter.postValue(it)
        }
    }

    suspend fun getAllClientsByDay() {
        clientDao.fetchClientsDay(_day.value?:0,_month.value?:0, _year.value?:0).collect() {
            _alClients.postValue(it)
            _alClientsFilter.postValue(it)
        }
    }

    suspend fun getAllClientsByMonth() {
        clientDao.fetchClientsMonth(month.value?:0, year.value?:0).collect() {
            _alClients.postValue(it)
            _alClientsFilter.postValue(it)
        }
    }

    suspend fun getAllClientsByYear() {
        clientDao.fetchClientsYear(_year.value?:0).collect() {
            _alClients.postValue(it)
            _alClientsFilter.postValue(it)
        }
    }

    fun filterByText(text: String, day: Int = 0, month: Int = 0, year: Int = 0) {
        if (text.trim().isNotEmpty()) {

            val filterList = alClients.value?.filter {
                it.name.contains(text, ignoreCase = true) || it.ci.contains(text, ignoreCase = true)
                        || it.phone.contains(text,ignoreCase = true)
                        || it.waterBomb.contains(text,ignoreCase = true)
            }

            _alClientsFilter.postValue(filterList)
        } else {
            _alClientsFilter.postValue(alClients.value)
        }
    }



    /*Add Info
    ---------
     */
    private fun insertClient(client: Client) {
        viewModelScope.launch {
            clientDao.insertClient(client)
        }
    }

    fun addNewClientEntry(
        ci: String,
        name: String,
        day: Int,
        month: Int,
        year: Int,
        phone: String,
        waterBomb: String,
        copli: String,
        imperente: String,
        price1: Double,
        monto1: String,
        price2: Double,
        monto2: String,
        warranty: Int,
        descWork: String,
        descClient: String
    ) {
        insertClient(
            Client(
                IDCreater.generate(),
                ci,
                name,
                day,
                month,
                year,
                phone,
                waterBomb,
                copli,
                imperente,
                price1,
                monto1,
                price2,
                monto2,
                warranty,
                descWork,
                descClient
            )
        )
    }



    /*Delete Info
    ---------
     */
    fun deleteClient(position: Int){
        viewModelScope.launch {
            alClientsFilter.value?.get(position)?.let { clientDao.deleteClient(it) }
        }
    }


    /*Update info
  ---------
   */
    fun updateProduct(client: Client){
        viewModelScope.launch {
            clientDao.updateClient(client)
        }
    }

}

class ClientViewModelFactory(
    private val clientDao: ClientDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowClientViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowClientViewModel(clientDao) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}