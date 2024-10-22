package com.example.admincravingcalories

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincravingcalories.Adapter.PendingOrderAdapter
import com.example.admincravingcalories.Models.OrderDetails
import com.example.admincravingcalories.databinding.ActivityPendingOrderBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity(),PendingOrderAdapter.OnItemClicked {
    private var listOfName:MutableList<String> = mutableListOf()
    private var listOfTotalPrice:MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder:MutableList<String> = mutableListOf()
    private var listOfOrderItem:ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails:DatabaseReference
    private val binding:ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backbutton.setOnClickListener {
            finish()
        }
        database = FirebaseDatabase.getInstance()
        databaseOrderDetails =database.reference.child("OrderDetails")
        gerOrderDetails()


    }


    private fun gerOrderDetails() {
        // retrive order Details
        databaseOrderDetails.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderSnapshot in snapshot.children){
                    val orderDetails = orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
             Toast.makeText(this@PendingOrderActivity,"not",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addDataToListForRecyclerView() {
        for(orderItem in listOfOrderItem){
       orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach {
                listOfImageFirstFoodOrder.add(it)
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingOrderRecyclerView.layoutManager =LinearLayoutManager(this)
        val adapter =PendingOrderAdapter(this,listOfName,listOfTotalPrice,listOfImageFirstFoodOrder,this)
        binding.pendingOrderRecyclerView.adapter =adapter
    }

    override fun onItemClickListener(position: Int) {
        val intent =Intent(this,orderDetailsActivity::class.java)
        val userOrderDetails =listOfOrderItem[position]
        intent.putExtra("UserOrderDetails",userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickListener(position: Int) {
        // handle item acceptence and update database
        val childItemPushKey =listOfOrderItem[position].itemPushKey
        val clickItemOrderReference = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderReference?.child("orderAccepted")?.setValue(true)
        updateOrderAccepteStatus(position)
    }



    override fun onItemDispatchClickListener(position: Int) {
      val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchItemOrderReferance= database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReferance.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrderDetails(dispatchItemPushKey)
            }


    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
        val orderDetailsItemReferance =database.reference.child("OrderDetails").child(dispatchItemPushKey)
       orderDetailsItemReferance.removeValue()
           .addOnSuccessListener {
               Toast.makeText(this, "Order is Dispatched", Toast.LENGTH_SHORT).show()
           }
           .addOnFailureListener {
               Toast.makeText(this, "Order is Not Dispatched", Toast.LENGTH_SHORT).show()
           }
    }

    private fun updateOrderAccepteStatus(position: Int) {
// update order acceptance in User's Buy history and orderdetails
        val userIdOfClickedItem =listOfOrderItem[position].userUid
        val pushKeyOfClickedItem  =listOfOrderItem[position].itemPushKey
        val buyHistoryReference =database.reference.child("user").child(userIdOfClickedItem!!).child("BuyHistory").child(pushKeyOfClickedItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)


    }
}
