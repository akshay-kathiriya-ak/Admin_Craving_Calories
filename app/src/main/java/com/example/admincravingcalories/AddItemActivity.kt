package com.example.admincravingcalories

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.admincravingcalories.Models.AllMenu
import com.example.admincravingcalories.databinding.ActivityAddItemBinding
import com.google.android.play.integrity.internal.f
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {
    private lateinit var foodName:String
    private lateinit var foodPrice:String
    private lateinit var foodDescription:String
    private lateinit var foodIngrediant:String
    private  var foodImageUri:Uri? =null

    private lateinit var auth :FirebaseAuth
    private lateinit var database:FirebaseDatabase

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        binding.backbutton.setOnClickListener {
            finish()
        }

        binding.AddItemButton.setOnClickListener {
            foodName =binding.enterFoodName.text.toString().trim()
            foodPrice =binding.enterFoodPrice.text.toString().trim()
            foodDescription =binding.desc.text.toString().trim()
            foodIngrediant =binding.Ingredients.text.toString().trim()

            if (!(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngrediant.isBlank()))
            {
                uploadData()
                Toast.makeText(this,"Item Add Successfully",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this,"Fill All Details",Toast.LENGTH_SHORT).show()

            }

        }
        binding.selectimage.setOnClickListener {
            pickimage.launch("image/*")
        }


    }

    private fun uploadData() {
       val MenuRef =database.getReference("Menu")
        // key
        val newItemKey =MenuRef.push().key


        if (foodImageUri != null){
            val storageRef = FirebaseStorage.getInstance().reference
            val     imageRef =storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask =imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    downloadUrl->
                    val newItem = AllMenu(
                        newItemKey ,
                        foodName =foodName,
                        foodPrice =foodPrice,
                        foodDescription  =foodDescription,
                         foodIngredient = foodIngrediant,
                        foodImage = downloadUrl.toString()
                        )
                    newItemKey?.let {
                        key ->MenuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this,"Data upload successfully",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this,"Data upload Fail",Toast.LENGTH_SHORT).show()
                    }
                    }
                }.addOnFailureListener {
                    Toast.makeText(this,"Image is not upload ",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {

            }
        }else{
            Toast.makeText(this,"Please Select Image",Toast.LENGTH_SHORT).show()

        }
    }

    val pickimage  = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri ->
        if (uri != null){
            binding.selectedimage.setImageURI(uri)
            foodImageUri= uri
        }

    }
}