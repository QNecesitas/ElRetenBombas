package com.qnecesitas.elretenbombas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import com.qnecesitas.elretenbombas.adapters.AdapterR_Client
import com.qnecesitas.elretenbombas.databinding.ActivityShowClientBinding

class Activity_ShowClient : AppCompatActivity() {


    //Binding
    private lateinit var binding: ActivityShowClientBinding

    //Date
    private var dateSelected = "Todo"
    private var year = 0
    private var month = 0
    private var day = 0

    //Recycler
    private lateinit var adapterrClient: AdapterR_Client

    //Filter
    private var lastFilterStr = ""





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowClientBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        //Spinner
        val alSpinner = arrayListOf(getString(R.string.todo),getString(R.string.Dia),getString(R.string.Mes), getString(R.string.Anno))
        val adapterSpinner = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, alSpinner);
        binding.spinner.adapter = adapterSpinner
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, long: Long) {
                val value: String = parent?.getItemAtPosition(position).toString()
                dateSelected = value
                when(value) {
                    "Todo"-> {
                        binding.ivDate.visibility = View.GONE
                        binding.tvDate.visibility = View.GONE
                        loadRecyclerInfoAll()
                    }
                    "Día" ->{
                        binding.ivDate.visibility = View.VISIBLE
                        binding.tvDate.visibility = View.VISIBLE
                        loadRecyclerInfoDay()
                    }
                    "Mes" ->{
                        binding.ivDate.visibility = View.VISIBLE
                        binding.tvDate.visibility = View.VISIBLE
                        loadRecyclerInfoMonth()
                    }
                    "Año" ->{
                        binding.ivDate.visibility = View.VISIBLE
                        binding.tvDate.visibility = View.VISIBLE
                        loadRecyclerInfoYear()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


        //SearchView
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterClient.getFilter()?.filter(newText)
                lastFilterStr = newText.toString()
                return false
            }

        })
        binding.ivIconSearch.setOnClickListener(View.OnClickListener {
            binding.acClSearch.visibility = View.VISIBLE
            binding.ivIconSearch.visibility = View.GONE
        })
        binding.ivCloseSearch.setOnClickListener(View.OnClickListener {
            binding.acClSearch.visibility = View.GONE
            binding.ivIconSearch.visibility = View.VISIBLE
        })


    }





    //LoadRecycler
    fun loadRecyclerInfoAll(){

    }

    fun loadRecyclerInfoDay(){

    }

    fun loadRecyclerInfoMonth(){

    }

    fun loadRecyclerInfoYear(){

    }

}