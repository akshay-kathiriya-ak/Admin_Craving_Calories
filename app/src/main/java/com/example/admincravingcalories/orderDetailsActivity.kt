package com.example.admincravingcalories

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincravingcalories.Adapter.OrderDetailsAdapter
import com.example.admincravingcalories.Models.OrderDetails
import com.example.admincravingcalories.databinding.ActivityOrderDetailsBinding

class orderDetailsActivity : AppCompatActivity() {
    private val binding : ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }
    private var userName :String? =null
    private var address :String? =null
    private var phoneNumber :String? =null
    private var totalPrice :String? =null
    private var foodNames:ArrayList<String> = arrayListOf()
    private var foodImages:ArrayList<String> = arrayListOf()
    private var foodQuantity:ArrayList<Int> = arrayListOf()
    private var foodPrices:ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
getDataFromIntent()
    }

    private fun getDataFromIntent() {
val recevedOrderDetails =intent.getSerializableExtra("UserOrderDetails")as OrderDetails
        recevedOrderDetails?.let { orderDetails ->
            if (recevedOrderDetails != null){
                userName = recevedOrderDetails.userName
                foodNames= recevedOrderDetails.foodNames  as ArrayList<String>
                foodImages =recevedOrderDetails.foodImages as ArrayList<String>
                foodQuantity = recevedOrderDetails.foodQuantities as ArrayList<Int>
                address = recevedOrderDetails.address
                phoneNumber =recevedOrderDetails.phoneNumber
                foodPrices =recevedOrderDetails.foodPrice as ArrayList<String>
                totalPrice =recevedOrderDetails.totalPrice

                setUserDetails()
                setAdapter()
            }
        }

    }

    private fun setAdapter() {
        binding.orderDetailsRecyclerView.layoutManager =LinearLayoutManager(this)
        val adapter =OrderDetailsAdapter(this,foodNames,foodImages,foodQuantity,foodPrices)
        binding.orderDetailsRecyclerView.adapter =adapter
    }

    private fun setUserDetails() {
        binding.Nameofuser.text =userName
        binding.Addressofuser.text =address
        binding.phonenoofuser.text =phoneNumber
        binding.totalpay.text =totalPrice
    }
}