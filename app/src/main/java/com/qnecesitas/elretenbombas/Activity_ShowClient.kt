package com.qnecesitas.elretenbombas

import android.content.DialogInterface
import android.content.Intent
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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.qnecesitas.elretenbombas.adapters.ClientAdapter
import com.qnecesitas.elretenbombas.data.AuxiliarEdit
import com.qnecesitas.elretenbombas.data.ClientViewModelFactory
import com.qnecesitas.elretenbombas.data.ShowClientViewModel
import com.qnecesitas.elretenbombas.database.Client
import com.qnecesitas.elretenbombas.databinding.ActivityShowClientBinding
import com.qnecesitas.elretenbombas.databinding.LiClientBinding
import com.qnecesitas.elretenbombas.databinding.LiDateYBinding
import com.qnecesitas.elretenbombas.databinding.LiDateYmBinding
import com.qnecesitas.elretenbombas.databinding.LiDateYmdBinding
import com.shashank.sony.fancytoastlib.FancyToast
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
        viewModel.saveLastYear(year)
        viewModel.saveLastMonth(month)
        viewModel.saveLastYear(year)
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
                viewModel.filterByText(newText.toString())
                return false
            }

        })
        binding.ivIconSearch.setOnClickListener(View.OnClickListener {
            binding.clSearch.visibility = View.VISIBLE
            binding.ivIconSearch.visibility = View.GONE
        })
        binding.ivCloseSearch.setOnClickListener(View.OnClickListener {
            binding.clSearch.visibility = View.GONE
            binding.ivIconSearch.visibility = View.VISIBLE
            binding.search.setQuery("",false)
        })


        //Settings
        binding.ivIconSetting.setOnClickListener{
            val intent = Intent(this, Activity_Settings::class.java)
            startActivity(intent)
        }

        //Recycler
        val clientAdapter = ClientAdapter(this@Activity_ShowClient)
        binding.rvClients.adapter = clientAdapter
        clientAdapter.setClick(object : ClientAdapter.ITouchCV{
            override fun onClick(position: Int) {
                li_client(position)
            }

        })

        //Btn Add
        binding.btnAcept.setOnClickListener{
            val intent = Intent(this, Activity_AddCustomer::class.java)
            startActivity(intent)
        }


        viewModel.alClientsFilter.observe(this, Observer {
            clientAdapter.submitList(it)
        })

        //Recycler
        loadRecyclerInfoAll()




    }





    //LoadRecycler
    @OptIn(DelicateCoroutinesApi::class)
    fun loadRecyclerInfoAll(){
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.getAllClients()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadRecyclerInfoDay(){
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.getAllClientsByDay()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadRecyclerInfoMonth(){
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.getAllClientsByMonth()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loadRecyclerInfoYear(){
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.getAllClientsByYear()
        }
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
            deleteSaleInternet(position)
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
        val name = viewModel.alClientsFilter.value?.get(position)?.name
        val CI = viewModel.alClientsFilter.value?.get(position)?.c_client
        val phone = viewModel.alClientsFilter.value?.get(position)?.phone
        val waterBomb = viewModel.alClientsFilter.value?.get(position)?.waterBomb
        val copli = viewModel.alClientsFilter.value?.get(position)?.copli
        val imperente = viewModel.alClientsFilter.value?.get(position)?.imperente
        val price = viewModel.alClientsFilter.value?.get(position)?.price1
        val warranty = viewModel.alClientsFilter.value?.get(position)?.warranty
        val descWork = viewModel.alClientsFilter.value?.get(position)?.descWork
        val descClient = viewModel.alClientsFilter.value?.get(position)?.descClient
        val date = getString(R.string.Fecha_fill,viewModel.alClientsFilter.value?.get(position)?.day.toString(),
            viewModel.alClientsFilter.value?.get(position)?.month.toString(),viewModel.alClientsFilter.value?.get(position)?.year.toString())

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
        liBinding.btnDelete.setOnClickListener{
            alertDialog.dismiss()
            showAlertCloseSales(position)
        }
        liBinding.btnEdit.setOnClickListener{
            alertDialog.dismiss()
            AuxiliarEdit.clientForEdit =
                viewModel.alClientsFilter.value?.get(position)
            val intent = Intent(this, Activity_EditClient::class.java)
            startActivity(intent)
        }

        //Finish
        alertDialog.setCancelable(false)
        alertDialog.window!!.setGravity(Gravity.CENTER)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun deleteSaleInternet(position: Int) {
        viewModel.deleteClient(position)
        FancyToast.makeText(
            this,
            getString(R.string.operacion_realizada_con_exito),
            FancyToast.LENGTH_SHORT,
            FancyToast.SUCCESS,
            false
        ).show()
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
            viewModel.saveLastYear(year)
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
            viewModel.saveLastYear(year)
            viewModel.saveLastMonth(month)
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
            viewModel.saveLastYear(year)
            viewModel.saveLastMonth(month)
            viewModel.saveLastDay(day)
            loadRecyclerInfoDay()
        }

        //Finish
        alertDialog.setCancelable(false)
        alertDialog.window!!.setGravity(Gravity.CENTER)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

    }

}