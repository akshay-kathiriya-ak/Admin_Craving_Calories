package com.example.admincravingcalories

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.admincravingcalories.Models.UserModel
import com.example.admincravingcalories.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SIgnUpActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var userName:String
    private lateinit var nameOfRestaurant:String
    private lateinit var database:DatabaseReference

    private val binding:ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth=Firebase.auth
        database=Firebase.database.reference
        binding.LoginButton1.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        binding.createUserButton.setOnClickListener {
            userName=binding.nameOfOwner.text.toString().trim()
            nameOfRestaurant=binding.nameOfRestaurant.text.toString().trim()
            email=binding.SignUpemailAddress1.text.toString().trim()
            password=binding.passwordforSignup.text.toString().trim()
            if(userName.isBlank() ||nameOfRestaurant.isBlank() ||email.isBlank() ||password.isBlank() ){
                Toast.makeText(this,"Please Fill All Details",Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }

        }


    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            task-> if (task.isSuccessful){
                Toast.makeText(this,"Account Create Successfully",Toast.LENGTH_SHORT).show()
            saveUserData()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
            }else{
            Toast.makeText(this,"Account Creation Failed",Toast.LENGTH_SHORT).show()
            }
        }
    }
// Save Data into Database
    private fun saveUserData() {
        userName=binding.nameOfOwner.text.toString().trim()
        nameOfRestaurant=binding.nameOfRestaurant.text.toString().trim()
        email=binding.SignUpemailAddress1.text.toString().trim()
        password=binding.passwordforSignup.text.toString().trim()
        val user = UserModel(userName,nameOfRestaurant,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}