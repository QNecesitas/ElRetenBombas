package com.qnecesitas.elretenbombas

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.media.audiofx.Equalizer.Settings
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.identity.PersonalizationData
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.qnecesitas.elretenbombas.auxiliary.Constants
import com.qnecesitas.elretenbombas.auxiliary.Permissions
import com.qnecesitas.elretenbombas.data.ClientViewModelFactory
import com.qnecesitas.elretenbombas.data.SettingsViewModel
import com.qnecesitas.elretenbombas.data.SettingsViewModelFactory
import com.qnecesitas.elretenbombas.data.ShowClientViewModel
import com.qnecesitas.elretenbombas.databinding.ActivitySettingsBinding
import com.shashank.sony.fancytoastlib.FancyToast

class Activity_Settings : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    //ViewModel
    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(
            (application as ElRetenApplication).database.clientDao()
        )
    }

    //Password
    private lateinit var password : String
    //Shared Preferences
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedEditor: SharedPreferences.Editor

    //Import
    val import = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri: Uri? ->
        uri?.let { viewModel.importBD(this@Activity_Settings,it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Preferences
        sharedPreferences = getSharedPreferences("ElRetenBomba", 0)
        sharedEditor = sharedPreferences.edit()
        password = sharedPreferences.getString("password", Constants.INITIAL_PASSWORD).toString()

        //Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnAdminAccept.setOnClickListener {
            clickAcceptPassword()
        }

        binding.clExpBd.setOnClickListener {
            exportDb()
        }
        binding.clImpBd.setOnClickListener{
            importDB()
        }


    }


    private fun clickAcceptPassword(){
        checkInfoPassword()
    }

    private fun checkInfoPassword(){
        val introducedPassword = binding.tietPasswAdminCurrent.text.toString()
        val newPassword = binding.tietPasswAdminNew.text.toString()

        var result = true

        if(binding.tietPasswAdminCurrent.text?.isEmpty() == true){
            result =false
            binding.tietPasswAdminCurrent.error = getString(R.string.Este_campo_no_debe)
        }else{
            binding.tietPasswAdminCurrent.error = null
        }

        if(binding.tietPasswAdminNew.text?.isEmpty() == true){
            result =false
            binding.tietPasswAdminNew.error = getString(R.string.Este_campo_no_debe)
        }else{
            binding.tietPasswAdminNew.error = null
        }

        if(binding.tietPasswAdminConfirm.text?.isEmpty() == true){
            result =false
            binding.tietPasswAdminConfirm.error = getString(R.string.Este_campo_no_debe)
        }else{
            binding.tietPasswAdminConfirm.error = null
        }

        if(binding.tietPasswAdminConfirm.text.toString() !=
            binding.tietPasswAdminNew.text.toString()){
            result =false
            binding.tietPasswAdminConfirm.error = getString(R.string.los_campos_no_coinciden)
        }else{
            binding.tietPasswAdminConfirm.error = null
        }

        if(introducedPassword == password){
            binding.tietPasswAdminCurrent.error = null
        }else{
            binding.tietPasswAdminCurrent.error = getString(R.string.Contrasena_incorrecta)
            result = false
        }

        if(result) updatePassword(newPassword)
    }

    private fun updatePassword(newPassword: String){
        sharedEditor.putString("password",newPassword)
        sharedEditor.apply()
        binding.tietPasswAdminCurrent.text = null
        binding.tietPasswAdminConfirm.text = null
        binding.tietPasswAdminNew.text = null
        FancyToast.makeText(
            this,
            getString(R.string.operacion_realizada_con_exito),
            FancyToast.LENGTH_SHORT,
            FancyToast.SUCCESS,
            false
        ).show()
    }

    private fun exportDb(){
        if(Permissions.siHayPermisoDeAlmacenamiento(this)) {
            viewModel.exportBD(this)
            showAlertDialogPathBd()
        }else{
            Permissions.pedirPermisoDeAlmacenamiento(this,viewModel.CODE_PERMISSION_STORAGE)
        }
    }

    private fun importDB(){
        if(Permissions.siHayPermisoDeAlmacenamiento(this)) {

            import.launch("*/*")
            showAlertDialogExit()

        }else{
            Permissions.pedirPermisoDeAlmacenamiento(this,viewModel.CODE_PERMISSION_STORAGE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == viewModel.CODE_PERMISSION_STORAGE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportDb()
            } else {
                showAlertDialogPermissionDenied()
            }
        }
    }

    private fun showAlertDialogPathBd() {
        //init alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle(R.string.Exportada_exito)
        builder.setMessage(R.string.Ubicacion_bd)
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.Aceptar) { dialog, _ ->
            //finish the activity
            dialog.dismiss()
        }
        //create the alert dialog and show it
        builder.create().show()
    }
    fun showAlertDialogPermissionDenied() {
        //init alert dialog
        val builder = android.app.AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(R.string.permiso_denegado)
        builder.setMessage(R.string.debe_otorgar_permisos_galeria)
        //set listeners for dialog buttons
        builder.setPositiveButton(
            R.string.Aceptar
        ) { dialog, _ -> dialog.dismiss() }
        //create the alert dialog and show it
        builder.create().show()
    }

    private fun showAlertDialogExit() {
        //init alert dialog
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle(R.string.importado_exito)
        builder.setMessage(R.string.Debe_reiniciar)
        //set listeners for dialog buttons
        builder.setPositiveButton(R.string.Si) { _, _ ->
            //finish the activity
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        //create the alert dialog and show it
        builder.create().show()
    }

}