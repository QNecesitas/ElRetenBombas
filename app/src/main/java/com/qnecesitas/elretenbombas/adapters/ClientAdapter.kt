package com.qnecesitas.elretenbombas.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.qnecesitas.elretenbombas.R
import com.qnecesitas.elretenbombas.database.Client
import com.qnecesitas.elretenbombas.databinding.RecyclerClientBinding

class ClientAdapter(private val context: Context) : ListAdapter<Client, ClientAdapter.ClientViewHolder>(DiffCallback) {

    private var clickClose: ClientAdapter.ITouchClose? = null
    private var click: ClientAdapter.ITouchCV? = null


    class ClientViewHolder(private var binding: RecyclerClientBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SimpleDateFormat")
        fun bind(client: Client, context: Context, clickCLose: ITouchClose?,
                 click: ITouchCV?) {

            //Declare
            var name = client.name
            var day = client.day
            var month = client.month
            var year = client.year


            binding.tvClient.text = name
            binding.tvDate.text = context.getString(R.string.Fecha,day.toString(),month.toString(),year.toString())

            binding.ivClose.setOnClickListener{ clickCLose?.onClickClose(position) }
            binding.cvContainer.setOnClickListener{ click?.onClick(position) }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder{
        val viewHolder = ClientViewHolder(
            RecyclerClientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener{
            val position = viewHolder.adapterPosition
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        holder.bind(getItem(position), context,clickClose,click)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Client>(){
            override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
                return oldItem == newItem
            }

        }
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