package com.example.admincravingcalories

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincravingcalories.Adapter.DeliveryAdapter
import com.example.admincravingcalories.Models.OrderDetails
import com.example.admincravingcalories.databinding.ActivityOutForDeliveryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding:ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private  var listOfCompletedOrderList:ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backbutton.setOnClickListener {
            finish()
        }
// retrive and display complete order
        retrieveCompleteOrderDetail()



    }

    private fun retrieveCompleteOrderDetail() {
        database =FirebaseDatabase.getInstance()
        val completeOrderReference=database.reference.child("CompletedOrder")
            .orderByChild("currentTime")
        completeOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompletedOrderList.clear()
                for (orderSnapshot in snapshot.children){
                   val completeOrder =orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompletedOrderList.add(it)
                    }
                }
                listOfCompletedOrderList.reverse()

                setDataIntroRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setDataIntroRecyclerView() {
        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()
        for(order in listOfCompletedOrderList){
            order.userName?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived)
        }
        val adapter =DeliveryAdapter(customerName, moneyStatus)
        binding.deliveryRecyclerView.adapter= adapter
        binding.deliveryRecyclerView.layoutManager =LinearLayoutManager(this)
    }
}