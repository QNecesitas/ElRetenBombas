package com.qnecesitas.elretenbombas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qnecesitas.elretenbombas.databinding.ActivityShowClientBinding

class Activity_ShowClient : AppCompatActivity() {


    //Binding
    private lateinit var binding: ActivityShowClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }


    }




}