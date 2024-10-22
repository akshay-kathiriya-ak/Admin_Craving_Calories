package com.example.admincravingcalories.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admincravingcalories.databinding.DeliveryItemBinding


class DeliveryAdapter(private val customerName:MutableList<String>,private val moneyStatus:MutableList<Boolean>):RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val binding = DeliveryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DeliveryViewHolder(binding)
    }

    override fun getItemCount(): Int =customerName.size

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bind(position)
    }
   inner class DeliveryViewHolder(private val binding:DeliveryItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                nameCustomer.text = customerName[position]
                if (moneyStatus[position] == true){
                    StatusOfMoney.text ="Received"
                }else{
                    StatusOfMoney.text ="NotReceived"
                }

                val colorMap = mapOf(
                    true to Color.GREEN,false to Color.RED
                )
                StatusOfMoney.setTextColor(colorMap[moneyStatus[position]]?:Color.BLACK)
                StatusColor.backgroundTintList= ColorStateList.valueOf(colorMap[moneyStatus[position]]?:Color.BLACK)

            }
        }

    }
}