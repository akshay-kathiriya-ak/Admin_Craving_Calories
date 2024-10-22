package com.example.admincravingcalories

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.admincravingcalories.Models.UserModel
import com.example.admincravingcalories.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private  var userName:String? = null
    private  var nameOfRestaurant:String? = null
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var database:DatabaseReference
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        auth =Firebase.auth
        database=Firebase.database.reference
       

        binding.LoginButton.setOnClickListener {
            email = binding.emailAddress1.text.toString().trim()
            password = binding.loginPassword.text.toString().trim()
            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Please Fill All Details", Toast.LENGTH_SHORT).show()
            }else{
                createUserAccount(email,password)

            }

        }


        binding.SignUpButton.setOnClickListener {
            val intent = Intent(this,SIgnUpActivity::class.java)
            startActivity(intent)
        }

    }
    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                // Save user data after successful account creation
                updateUi(user)
            }  else {
                Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }



}