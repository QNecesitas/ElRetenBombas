package com.qnecesitas.elretenbombas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qnecesitas.elretenbombas.databinding.ActivityLoginBinding
import com.qnecesitas.elretenbombas.databinding.ActivityShowClientBinding

class Activity_Login : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }



}