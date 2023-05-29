package com.qnecesitas.elretenbombas

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qnecesitas.elretenbombas.auxiliary.Constants
import com.qnecesitas.elretenbombas.databinding.ActivitySettingsBinding

class Activity_Settings : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    //Password
    private lateinit var password : String
    //Shared Preferences
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Preferences
        sharedPreferences = getSharedPreferences("ElRetenBomba", 0)
        sharedEditor = sharedPreferences.edit()
        password = sharedPreferences.getString("password", Constants.INITIAL_PASSWORD).toString()

        binding.btnAdminAccept.setOnClickListener {
            clickAcceptPassword()
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
            binding.tietPasswAdminConfirm.error = null
        }else{
            binding.tietPasswAdminConfirm.error = getString(R.string.Contrasena_incorrecta)
            result = false
        }

        if(result) updatePassword(newPassword)
    }

    private fun updatePassword(newPassword: String){
        sharedEditor.putString("password",newPassword)
        sharedEditor.apply()
    }

}