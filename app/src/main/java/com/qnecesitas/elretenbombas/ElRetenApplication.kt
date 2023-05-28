package com.qnecesitas.elretenbombas

import android.app.Application
import com.qnecesitas.elretenbombas.database.AppDatabase

class ElRetenApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}