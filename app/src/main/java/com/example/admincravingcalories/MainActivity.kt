package com.example.admincravingcalories

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.admincravingcalories.Models.OrderDetails
import com.example.admincravingcalories.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
       binding.Addmenu.setOnClickListener {
    val intent = Intent(this,AddItemActivity::class.java)
    startActivity(intent)
       }
        binding.AllItemMenu.setOnClickListener{
            val intent = Intent(this,All_itemActivity::class.java)
            startActivity(intent)
        }
        binding.OutForDeliveryButton.setOnClickListener{
            val intent = Intent(this,OutForDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.OutForDeliveryButton.setOnClickListener{
            val intent = Intent(this,OutForDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.AdminProfile.setOnClickListener{
            val intent = Intent(this,AdminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.pendingOrder.setOnClickListener {
            val intent = Intent(this,PendingOrderActivity::class.java)
            startActivity(intent)
        }
        binding.logOutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,SIgnUpActivity::class.java))
            finish()
        }
        pendingOrder()
        completedOrder()
        wholeTimeEarning()
    }

    private fun wholeTimeEarning() {
        var listOfTotalPay = mutableListOf<Int>()
        database=FirebaseDatabase.getInstance()
        completedOrderReference =database.reference.child("CompletedOrder")

        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for (orderSnapshot in snapshot.children){
                   var completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                   completeOrder?.totalPrice?.replace("","")?.toIntOrNull()
                       ?.let { i->
                           listOfTotalPay.add(i)
                       }
               }

                binding.wholeTimeEarning.text = listOfTotalPay.sum().toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun completedOrder() {
        database=FirebaseDatabase.getInstance()
        var completedOrderReference =database.reference.child("CompletedOrder")
        var completedOrderItemCount =0
        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completedOrderItemCount= snapshot.childrenCount.toInt()
                binding.completeOrder.text = completedOrderItemCount.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun pendingOrder() {
         database=FirebaseDatabase.getInstance()
        var pendingOrderReference =database.reference.child("OrderDetails")
        var pendingOrderItemCount =0
        pendingOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount= snapshot.childrenCount.toInt()
                binding.pendingOrders.text =pendingOrderItemCount.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}