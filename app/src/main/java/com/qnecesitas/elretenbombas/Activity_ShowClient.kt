package com.qnecesitas.elretenbombas

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.qnecesitas.elretenbombas.adapters.AdapterR_Client
import com.qnecesitas.elretenbombas.adapters.ClientAdapter
import com.qnecesitas.elretenbombas.data.ClientViewModelFactory
import com.qnecesitas.elretenbombas.data.ShowClientViewModel
import com.qnecesitas.elretenbombas.database.Client
import com.qnecesitas.elretenbombas.databinding.ActivityShowClientBinding
import com.qnecesitas.elretenbombas.databinding.LiClientBinding
import com.qnecesitas.elretenbombas.databinding.LiDateYBinding
import com.qnecesitas.elretenbombas.databinding.LiDateYmBinding
import com.qnecesitas.elretenbombas.databinding.LiDateYmdBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Calendar

class Activity_ShowClient : AppCompatActivity() {


    //Binding
    private lateinit var binding: ActivityShowClientBinding

    //Date
    private var dateSelected = "Todo"
    private var year = 0
    private var month = 0
    private var day = 0

    //Filter
    private var lastFilterStr = ""
    private val alClient = ArrayList<Client>()

    //ViewModel
    private val viewModel: ShowClientViewModel by viewModels {
        ClientViewModelFactory(
            (application as ElRetenApplication).database.clientDao()
        )
    }


    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowClientBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        //Date
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        binding.ivDate.setOnClickListener(View.OnClickListener {
            select_datePick()
        })

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
                //adapterrClient.getFilter()?.filter(newText)
                //TODO
                lastFilterStr = newText.toString()
                return false
            }

        })
        binding.ivIconSearch.setOnClickListener(View.OnClickListener {
            binding.search.visibility = View.VISIBLE
            binding.ivIconSearch.visibility = View.GONE
        })
        binding.ivCloseSearch.setOnClickListener(View.OnClickListener {
            binding.search.visibility = View.GONE
            binding.ivIconSearch.visibility = View.VISIBLE
        })

        //Refresh
        binding.refresh.setOnRefreshListener {
            when(dateSelected){
                "Todo" -> loadRecyclerInfoAll()
                "Año" -> loadRecyclerInfoYear()
                "Mes" -> loadRecyclerInfoMonth()
                "Día" -> loadRecyclerInfoDay()
            }
        }


        //Recycler
        val clientAdapter = ClientAdapter(this@Activity_ShowClient)
        binding.rvClients.adapter = clientAdapter
        clientAdapter.setCloseClick(object : ClientAdapter.ITouchClose{
            override fun onClickClose(position: Int) {
                showAlertCloseSales(position)
            }
        })
        clientAdapter.setClick(object : ClientAdapter.ITouchCV{
            override fun onClick(position: Int) {
                li_client(position)
            }

        })


        //Recycler
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.getAllClients().collect(){
                clientAdapter.submitList(it)
            }
        }

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

    /*Close
    ------------------
     */
    private fun showAlertCloseSales(position: Int){
        //init alert dialog
        val builder = android.app.AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(R.string.Eliminar_cliente)
        builder.setMessage(R.string.Desea_eliminar_cliente)
        //set listeners for dialog buttons
        builder.setPositiveButton(
            R.string.Si
        ) { dialog, _ ->
            dialog.dismiss()
            deleteSaleInternet(alClient[position].c_client, position)
        }
        builder.setNegativeButton(R.string.No,
            DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })

        //create the alert dialog and show it
        builder.create().show()
    }

    private fun li_client(position: Int){
        val inflater = LayoutInflater.from(binding.root.context)
        val liBinding = LiClientBinding.inflate(inflater)
        val builder = AlertDialog.Builder(binding.root.context)
        builder.setView(liBinding.root)
        val alertDialog = builder.create()

        //Init
        val name = alClient[position].name
        val CI = alClient[position].c_client
        val phone = alClient[position].phone
        val waterBomb = alClient[position].waterBomb
        val copli = alClient[position].copli
        val imperente = alClient[position].imperente
        val price = alClient[position].price
        val warranty = alClient[position].warranty
        val descWork = alClient[position].descWork
        val descClient = alClient[position].descClient
        val date = getString(R.string.Fecha,alClient[position].day.toString(),
            alClient[position].month.toString(),alClient[position].year.toString())

        //Fill out
        liBinding.tvName.text = name
        liBinding.tvCI.text = CI.toString()
        liBinding.tvPhone.text = phone
        liBinding.tvType.text = waterBomb
        liBinding.tvCopli.text = copli
        liBinding.tvImp.text = imperente
        liBinding.tvPrice.text = price.toString()
        liBinding.tvDate.text = date
        liBinding.tvGarantia.text = warranty.toString()
        liBinding.tvDescCli.text = descClient
        liBinding.tvDesc.text = descWork

        //Listeners
        liBinding.ivClose.setOnClickListener{
            alertDialog.dismiss()
        }

        //Finish
        alertDialog.setCancelable(false)
        alertDialog.window!!.setGravity(Gravity.CENTER)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun deleteSaleInternet(orderCode: Int, position: Int) {

    }

    /*Date Pickers
* ---------------------------------
* */
    fun select_datePick() {
        when (dateSelected) {
            "Todo" -> li_dateYear()
            "Día" -> li_dateDay()
            "Mes" -> li_dateMonth()
            "Año" -> li_dateYear()
        }
    }

    fun li_dateYear(){
        val inflater = LayoutInflater.from(binding.root.context)
        val liBinding = LiDateYBinding.inflate(inflater)
        val builder = AlertDialog.Builder(binding.root.context)
        builder.setView(liBinding.root)
        val alertDialog = builder.create()

        //Declare
        liBinding.ilNpAnno.maxValue = 2050
        liBinding.ilNpAnno.minValue = 2020

        //Listeners
        liBinding.btnCancel.setOnClickListener{
            alertDialog.dismiss()
        }
        liBinding.btnAcept.setOnClickListener{
            alertDialog.dismiss()
            year = liBinding.ilNpAnno.value
            binding.tvDate.text = year.toString()
            loadRecyclerInfoYear()
        }

        //Finish
        alertDialog.setCancelable(false)
        alertDialog.window!!.setGravity(Gravity.CENTER)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

    }

    fun li_dateMonth(){

        val inflater = LayoutInflater.from(binding.root.context)
        val liBinding = LiDateYmBinding.inflate(inflater)
        val builder = AlertDialog.Builder(binding.root.context)
        builder.setView(liBinding.root)
        val alertDialog = builder.create()

        //Declare
        liBinding.ilNpAnnos.maxValue = 2050
        liBinding.ilNpAnnos.minValue = 2020
        liBinding.ilNpMonth.maxValue = 11
        liBinding.ilNpMonth.minValue = 0
        val months = arrayOf("Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre")
        liBinding.ilNpMonth.displayedValues = months

        //Listeners
        liBinding.btnCancel.setOnClickListener{
            alertDialog.dismiss()
        }
        liBinding.btnAcept.setOnClickListener{
            alertDialog.dismiss()
            year = liBinding.ilNpAnnos.value
            month = liBinding.ilNpMonth.value+1
            val dateDisplay="${year}/${month}"
            binding.tvDate.text = dateDisplay
            loadRecyclerInfoMonth()
        }

        //Finish
        alertDialog.setCancelable(false)
        alertDialog.window!!.setGravity(Gravity.CENTER)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

    }

    fun li_dateDay(){

        val inflater = LayoutInflater.from(binding.root.context)
        val liBinding = LiDateYmdBinding.inflate(inflater)
        val builder = AlertDialog.Builder(binding.root.context)
        builder.setView(liBinding.root)
        val alertDialog = builder.create()

        //Declare
        liBinding.ilNpAnnos.maxValue = 2050
        liBinding.ilNpAnnos.minValue = 2020
        liBinding.ilNpMonth.maxValue = 11
        liBinding.ilNpMonth.minValue = 0
        liBinding.ilNpDay.minValue = 1
        liBinding.ilNpDay.maxValue = 31
        val months = arrayOf("Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre")
        liBinding.ilNpMonth.displayedValues = months

        //Listeners
        liBinding.btnCancel.setOnClickListener{
            alertDialog.dismiss()
        }
        liBinding.btnAcept.setOnClickListener{
            alertDialog.dismiss()
            year = liBinding.ilNpAnnos.value
            month = liBinding.ilNpMonth.value + 1
            day = liBinding.ilNpDay.value
            val dateDisplay="${year}/${month}/${day}"
            binding.tvDate.text = dateDisplay
            loadRecyclerInfoDay()
        }

        //Finish
        alertDialog.setCancelable(false)
        alertDialog.window!!.setGravity(Gravity.CENTER)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

    }

}