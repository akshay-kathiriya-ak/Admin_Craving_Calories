package com.example.admincravingcalories

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.admincravingcalories.Models.UserModel
import com.example.admincravingcalories.databinding.ActivityAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding : ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        database =FirebaseDatabase.getInstance()
        adminReference =database.reference.child("user")
        binding.saveInfo.setOnClickListener {
            updateUserData()
        }
        binding.backbutton.setOnClickListener {
            finish()
        }
        binding.name.isEnabled = false
        binding.address.isEnabled = false
        binding.email.isEnabled = false
        binding.phonenumber.isEnabled = false
        binding.password.isEnabled = false
        binding.saveInfo.isEnabled =false

        var isEnable =false
        binding.EditProfile.setOnClickListener {
            isEnable =! isEnable
            binding.name.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.phonenumber.isEnabled = isEnable
            binding.password.isEnabled = isEnable

            if (isEnable){
                binding.name.requestFocus()
            }
            binding.saveInfo.isEnabled =isEnable

        }
        retriveUserData()

    }



    private fun retriveUserData() {
        val currentUser =auth.currentUser?.uid
        if (currentUser != null){
            val userReference =adminReference.child(currentUser)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        var ownerName =snapshot.child("name").getValue()
                        var email =snapshot.child("email").getValue()
                        var password =snapshot.child("password").getValue()
                        var address =snapshot.child("address").getValue()
                        var phone =snapshot.child("phone").getValue()
                        setDataToTextView(ownerName,email,password,address,phone)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }

    private fun setDataToTextView(
        ownerName: Any?,
        email: Any?,
        password: Any?,
        address: Any?,
        phone: Any?
    ) {


           binding.name.setText(ownerName.toString())
           binding.email.setText(email.toString())
           binding.password.setText(password.toString())
           binding.phonenumber.setText(phone.toString())
           binding.address.setText(address.toString())

    }
    private fun updateUserData() {
var updateName  = binding.name.text.toString()
        var updateEmail=   binding.email.text.toString()
        var updatePassword=binding.password.text.toString()
        var updatePhone= binding.phonenumber.text.toString()
        var updateAddress=binding.address.text.toString()
val currentUserUid =auth.currentUser?.uid
        if (currentUserUid != null){
            val userReference =adminReference.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("password").setValue(updatePassword)
            userReference.child("address").setValue(updateAddress)
            userReference.child("phone").setValue(updatePhone)

            Toast.makeText(this,"profile Update Successfully",Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)

        }


    }
}