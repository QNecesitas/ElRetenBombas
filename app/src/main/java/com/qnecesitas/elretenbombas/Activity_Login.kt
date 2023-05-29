package com.qnecesitas.elretenbombas

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qnecesitas.elretenbombas.auxiliary.Constants
import com.qnecesitas.elretenbombas.databinding.ActivityLoginBinding

class Activity_Login : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityLoginBinding

    //Password
    private lateinit var password: String

    //Shared Preferences
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedEditor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)




        //Preferences
        sharedPreferences = getSharedPreferences("ElRetenBomba", 0)
        sharedEditor = sharedPreferences.edit()


        password = sharedPreferences.getString("password", Constants.INITIAL_PASSWORD).toString()

        binding.ALBTNStartSession.setOnClickListener {
            if (binding.ALTIETPassword.text.toString().trim().isNotEmpty()){
                if(binding.ALTIETPassword.text.toString() == password){
                    binding.ALTILPassword.error = null

                    val intent = Intent(this, Activity_ShowClient::class.java)
                    startActivity(intent)

                }else{
                    binding.ALTILPassword.error = getString(R.string.Contrasena_incorrecta)
                }
            }else{
                binding.ALTILPassword.error = getString(R.string.Este_campo_no_debe)
            }
        }


    }



}