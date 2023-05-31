package com.qnecesitas.elretenbombas.auxiliary

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permissions {

    companion object {
        //Comprobar si hay permisos
        fun siHayPermisoDeAlmacenamiento(context: Context?): Boolean {
            val result =
                ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            return result == PackageManager.PERMISSION_GRANTED
        }

        //PedirPermisos
        fun pedirPermisoDeAlmacenamiento(activity: Activity, STORAGE_PERMISSION_CODE: Int) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

}