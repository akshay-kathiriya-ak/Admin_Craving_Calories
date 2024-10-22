package com.example.admincravingcalories.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.admincravingcalories.databinding.PendingItemBinding

class PendingOrderAdapter(
    private val context: Context,
    private val customerNames: MutableList<String>,
    private val quntityofFood: MutableList<String>,
    private val foodImage: MutableList<String>,
    private val itemClicked:OnItemClicked,
) : RecyclerView.Adapter<PendingOrderAdapter.PedingOrderViewHolder>() {
    interface OnItemClicked{
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedingOrderViewHolder {
        val binding = PendingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PedingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PedingOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class PedingOrderViewHolder(private val binding: PendingItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false

        fun bind(position: Int) {
            if (position in customerNames.indices && position in quntityofFood.indices && position in foodImage.indices) {
                binding.apply {
                    NameofCustomer.text = customerNames[position]
                    pendingOrderquntity.text = quntityofFood[position]
                    val uri = Uri.parse(foodImage[position])
                    Glide.with(context).load(uri).into(OrderImage)

                    orderAcceptButton.apply {
                        text = if (!isAccepted) "Accept" else "Dispatch"

                        setOnClickListener {
                            if (!isAccepted) {
                                text = "Dispatch"
                                Toast.makeText(context, "Order is Accepted", Toast.LENGTH_SHORT).show()
                                itemClicked.onItemAcceptClickListener(position)
                                isAccepted = true
                            } else {
                                // Remove the item from all lists at the same position
                                customerNames.removeAt(adapterPosition)
                                quntityofFood.removeAt(adapterPosition)
                                foodImage.removeAt(adapterPosition)

                                // Notify the adapter about the item being removed
                                notifyItemRemoved(adapterPosition)
                                Toast.makeText(context, "Order is Dispatched", Toast.LENGTH_SHORT).show()
                                itemClicked.onItemDispatchClickListener(position)
                            }
                        }
                    }
                    itemView.setOnClickListener {
                        itemClicked.onItemClickListener(position)
                    }

                }
            } else {
                Toast.makeText(context, "Error: Invalid item position", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
