package com.qnecesitas.elretenbombas.data

import android.content.ContentValues
import android.content.Context
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
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.time.Year

class SettingsViewModel(private val clientDao: ClientDao) : ViewModel() {

    val CODE_PERMISSION_STORAGE = 111

    fun exportBD(context: Context) {
        val database = AppDatabase.getDatabase(context)


        val filename = "app_database.db"
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, filename)
        val currentDbPath = context.getDatabasePath(database.openHelper.databaseName)

        try {
            database.close()
            val fileOutputStream = FileOutputStream(file)
            currentDbPath.copyTo(file,overwrite = true)
            fileOutputStream.close()

        }catch (e: Exception){
            Log.e("Error",e.toString())
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

}

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