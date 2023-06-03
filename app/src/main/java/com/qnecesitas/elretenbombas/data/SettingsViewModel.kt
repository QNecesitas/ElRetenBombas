package com.qnecesitas.elretenbombas.data

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.FileUtils
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.qnecesitas.elretenbombas.auxiliary.IDCreater
import com.qnecesitas.elretenbombas.database.AppDatabase
import com.qnecesitas.elretenbombas.database.Client
import com.qnecesitas.elretenbombas.database.ClientDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.lang.StringBuilder
import java.time.Year

class SettingsViewModel(private val clientDao: ClientDao) : ViewModel() {

    fun exportBD(context: Context, outputStream: OutputStream?) {
        val database = AppDatabase.getDatabase(context)
        val currentDbPath = context.getDatabasePath(database.openHelper.databaseName)

        try {
            database.close()
            outputStream?.let { output ->
                val inputChannel = FileInputStream(currentDbPath).channel
                val outputChannel = (output as FileOutputStream).channel
                inputChannel.transferTo(0, inputChannel.size(), outputChannel)
                inputChannel.close()
                outputChannel.close()
            }
            outputStream?.close()
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
    }

    fun importBD(context: Context, uri: Uri){
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.let {
            val importDir = File(context.getExternalFilesDir(null),"app_database.db")
            val outputStream = FileOutputStream(importDir)
            inputStream.copyTo(outputStream)
            outputStream.close()

            if(importDir.exists()){
                val appDatabase = AppDatabase.getDatabase(context)
                val currentDatabasePath = context.getDatabasePath(appDatabase.openHelper.databaseName)
                appDatabase.close()
                importDir.copyTo(currentDatabasePath,overwrite = true)
                importDir.delete()
            }

        }
    }

  /*  fun getBDDate(context: Context){
        var data: Flow<List<Client>> = AppDatabase.getDatabase(context).clientDao().fetchClients()

        var contentBuilder: StringBuilder
        for (item: Client in data){
            contentBuilder.append(item.toString())
            contentBuilder.append("\n")
        }
        var content: String = contentBuilder.toString()



}*/

class SettingsViewModelFactory(
    private val clientDao: ClientDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(clientDao) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}
    }