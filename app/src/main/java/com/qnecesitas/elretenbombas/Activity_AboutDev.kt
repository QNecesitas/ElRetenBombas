package com.qnecesitas.elretenbombas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qnecesitas.elretenbombas.databinding.ActivityAboutDevBinding
import com.qnecesitas.elretenbombas.databinding.ActivityAboutUsBinding

class Activity_AboutDev : AppCompatActivity() {

    private lateinit var binding: ActivityAboutDevBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutDevBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Toolbar
        setSupportActionBar(binding.AADToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.AADToolbar.setNavigationOnClickListener { finish() }

    }
}