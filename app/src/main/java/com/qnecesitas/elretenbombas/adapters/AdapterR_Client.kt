package com.qnecesitas.elretenbombas.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.qnecesitas.elretenbombas.R
import com.qnecesitas.elretenbombas.data.ModelClient
import com.qnecesitas.elretenbombas.databinding.RecyclerClientBinding
import java.util.Locale

class AdapterR_Client(val alClient: ArrayList<ModelClient>, private val context: Context):
    RecyclerView.Adapter<AdapterR_Client.ClientViewHolder>(){

    private var customFilter: AdapterR_Client.CustomFilter? = null
    private var alFilter: ArrayList<ModelClient> = ArrayList()
    private var clickClose: ITouchClose? = null
    private var click: ITouchCV? = null

    class ClientViewHolder(private val binding: RecyclerClientBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(
            modelClient: ModelClient,
            context: Context,
            position: Int,
            clickCLose: ITouchClose?,
            click: ITouchCV?
        ){
            val cClient = modelClient.c_client
            val nombre = modelClient.name
            val dia = modelClient.day.toString()
            val mes = modelClient.month.toString()
            val anio = modelClient.year.toString()

            binding.tvClient.text = nombre
            binding.tvDate.text = context.getString(R.string.Fecha,dia,mes,anio)


            binding.ivClose.setOnClickListener{ clickCLose?.onClickClose(position) }
            binding.cvContainer.setOnClickListener{ click?.onClick(position) }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerClientBinding.inflate(layoutInflater,parent,false)
        return ClientViewHolder(binding)
    }

    override fun getItemCount() = alClient.size

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bind(alClient[position],context,position,clickClose,click)
    }

    init {
        alFilter.addAll(alClient)
        customFilter = CustomFilter(this)
    }


    /*Filter elements
    *------------------
     */
    //Class custom
    inner class CustomFilter(adapterR_sales: AdapterR_Client) :
        Filter() {
        var adapterR_sales: AdapterR_Client

        init {
            this.adapterR_sales = adapterR_sales
        }

        override fun performFiltering(charSequence: CharSequence): FilterResults {
            alFilter.clear()
            val filterResults = FilterResults()
            if (charSequence.length == 0) {
                alFilter.addAll(alClient)
            } else {
                val filterPattern =
                    charSequence.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (product in alClient) {
                    if (product.name.lowercase(Locale.ROOT).trim().contains(filterPattern)) {
                        alFilter.add(product)
                    }
                }
            }
            filterResults.values = alFilter
            filterResults.count = alFilter.size
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            adapterR_sales.notifyDataSetChanged()
        }
    }

    //Real position
    private fun getRealPosition(irealPosition: Int): Int{
        for((index, model) in alClient.withIndex()){
            if(model == alFilter[irealPosition]){
                return index
            }
        }
        return 0
    }

    //Custom
    fun getFilter(): Filter? {
        return customFilter
    }

    //Close
    interface ITouchClose{
        fun onClickClose(position: Int)
    }
    fun setCloseClick(clickClose: ITouchClose){
        this.clickClose = clickClose
    }

    //Touch CV
    interface ITouchCV{
        fun onClick(position: Int)
    }
    fun setClick(click: ITouchCV){
        this.click = click
    }

}