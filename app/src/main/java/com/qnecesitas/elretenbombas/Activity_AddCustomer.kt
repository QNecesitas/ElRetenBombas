package com.qnecesitas.elretenbombas

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.qnecesitas.elretenbombas.data.ClientViewModelFactory
import com.qnecesitas.elretenbombas.data.ShowClientViewModel
import com.qnecesitas.elretenbombas.database.Client
import com.qnecesitas.elretenbombas.databinding.ActivityAddCustomerBinding
import com.qnecesitas.elretenbombas.databinding.LiDateYmdBinding
import com.shashank.sony.fancytoastlib.FancyToast
import java.util.Calendar

class Activity_AddCustomer : AppCompatActivity() {

    private val viewModel: ShowClientViewModel by viewModels {
        ClientViewModelFactory(
            (application as ElRetenApplication).database.clientDao()
        )
    }

    private lateinit var binding: ActivityAddCustomerBinding


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        //Initial date
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        val date = "${day}/${month}/${year}"
        binding.TIETDate.setText(date)
        viewModel.saveLastDay(day)
        viewModel.saveLastMonth(month)
        viewModel.saveLastYear(year)


        //Spinner
        val alType = arrayListOf("CUP","USD","MLC")
        val stringArrayAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alType)
        binding.TIETMonto.setAdapter(stringArrayAdapter)
        binding.TIETMontoH.setAdapter(stringArrayAdapter)

        //Listeners
        binding.TIETDate.setOnClickListener {
            liDateDay()
        }

        binding.SWM.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked){
                binding.llMontoH.visibility = View.VISIBLE
            }else{
                binding.llMontoH.visibility = View.GONE
            }
        }

        binding.btnAcept.setOnClickListener {
            if (isEntryValid()) {
                showAlertConfirm()
            }
        }

    }


    //Date
    private fun liDateDay() {

        val inflater = LayoutInflater.from(binding.root.context)
        val liBinding = LiDateYmdBinding.inflate(inflater)
        val builder = AlertDialog.Builder(binding.root.context)
        builder.setView(liBinding.root)
        val alertDialog = builder.create()

        //Declare
        liBinding.ilNpAnnos.maxValue = 2050
        liBinding.ilNpAnnos.minValue = 2020
        liBinding.ilNpAnnos.value = viewModel.year.value?:2020
        liBinding.ilNpMonth.maxValue = 11
        liBinding.ilNpMonth.minValue = 0
        val month = viewModel.month.value?:2
        liBinding.ilNpMonth.value = month-1
        liBinding.ilNpDay.minValue = 1
        liBinding.ilNpDay.maxValue = 31
        liBinding.ilNpDay.value = viewModel.day.value?:1
        val months = arrayOf(
            "Enero",
            "Febrero",
            "Marzo",
            "Abril",
            "Mayo",
            "Junio",
            "Julio",
            "Agosto",
            "Septiembre",
            "Octubre",
            "Noviembre",
            "Diciembre"
        )
        liBinding.ilNpMonth.displayedValues = months

        //Listeners
        liBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        liBinding.btnAcept.setOnClickListener {
            alertDialog.dismiss()
            val year = liBinding.ilNpAnnos.value
            val month = liBinding.ilNpMonth.value + 1
            val day = liBinding.ilNpDay.value
            val date = "${day}/${month}/${year}"
            viewModel.saveLastDay(day)
            viewModel.saveLastMonth(month)
            viewModel.saveLastYear(year)
            binding.TIETDate.setText(date)
        }

        //Finish
        alertDialog.setCancelable(false)
        alertDialog.window!!.setGravity(Gravity.CENTER)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun isEntryValid(): Boolean {

        if (binding.TIETWarranty.text.toString().trim().isNotEmpty()) {
            binding.TIETWarranty.error = null
        } else {
            binding.TIETWarranty.error = getString(R.string.Este_campo_no_debe)
            return false
        }

        if (binding.TIETPrecio.text.toString().trim().isNotEmpty()) {
            binding.TIETPrecio.error = null
        } else {
            binding.TIETPrecio.error = getString(R.string.Este_campo_no_debe)
            return false
        }

        if (binding.TIETDate.text.toString().trim().isNotEmpty()) {
            binding.TIETDate.error = null
        } else {
            binding.TIETDate.error = getString(R.string.Este_campo_no_debe)
            return false
        }

        if (binding.TIETPrecioH.text.toString().trim().isNotEmpty() ) {
            binding.TIETPrecioH.error = null
        } else {
            binding.TIETPrecioH.error = getString(R.string.Este_campo_no_debe)
            binding.SWM.isChecked = true
            return false
        }


        return true
    }

    //Information
    private fun saveInformation() {
        viewModel.addNewClientEntry(
            binding.TIETCi.text.toString(),
            binding.TIETName.text.toString(),
            viewModel.day.value ?: 1,
            viewModel.month.value ?: 1,
            viewModel.year.value ?: 2023,
            binding.TIETPhone.text.toString(),
            binding.TIETWaterBomb.text.toString(),
            binding.TIETCopli.text.toString(),
            binding.TIETImperente.text.toString(),
            binding.TIETPrecio.text.toString().toDouble(),
            binding.TIETMonto.text.toString(),
            binding.TIETPrecioH.text.toString().toDouble(),
            binding.TIETMontoH.text.toString(),
            binding.TIETWarranty.text.toString().toInt(),
            binding.TIETDescWork.text.toString(),
            binding.TIETDescClient.text.toString()
        )
        FancyToast.makeText(
            this,
            getString(R.string.operacion_realizada_con_exito),
            FancyToast.LENGTH_SHORT,
            FancyToast.SUCCESS,
            false
        ).show()
        finish()
    }

    private fun showAlertConfirm() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setCancelable(true)
            .setTitle(getString(R.string.Guardar_cliente))
            .setMessage(getString(R.string.Tiene_seguridad_guardar))
            .setPositiveButton(R.string.Aceptar) { dialog, _ ->
                dialog.dismiss()
                saveInformation()
            }
            .setNegativeButton(R.string.Cancelar) { dialog, _ ->
                dialog.dismiss()
            }

        //create the alert dialog and show it
        builder.create().show()
    }

}