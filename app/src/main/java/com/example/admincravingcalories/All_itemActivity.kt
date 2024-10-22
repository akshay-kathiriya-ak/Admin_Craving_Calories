package com.example.admincravingcalories

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admincravingcalories.Adapter.MenuItemAdapter
import com.example.admincravingcalories.Models.AllMenu
import com.example.admincravingcalories.databinding.ActivityAllItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class All_itemActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private  var menuItems: ArrayList<AllMenu> = ArrayList()
    private val binding:ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backbutton.setOnClickListener {
            finish()
        }

        databaseReference =FirebaseDatabase.getInstance().reference
        retriveMenuItem()



    }

    private fun retriveMenuItem() {
        database =FirebaseDatabase.getInstance()
        val foodRef :   DatabaseReference =database.reference.child("Menu")

        // fetch data
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //
                menuItems.clear()
                    //
                for (foodSnapshot in snapshot.children){
                    val menuItem =foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setAdapter() {
        val adapter = MenuItemAdapter(this@All_itemActivity,menuItems,databaseReference,){position->
            deleteMenuItem(position)
        }
        binding.menuRecylerView.layoutManager =LinearLayoutManager(this)
        binding.menuRecylerView.adapter =adapter
    }

    private fun deleteMenuItem(position: Int) {
        val menuItemToDelete =menuItems[position]
        val menuItemKey= menuItemToDelete.key
        val foodMenuReference =database.reference.child("Menu").child(menuItemKey!!)
        foodMenuReference.removeValue().addOnCompleteListener {task->
            if (task.isSuccessful){
                menuItems.removeAt(position)
                binding.menuRecylerView.adapter?.notifyItemRemoved(position)
            }else{
                Toast.makeText(this,"Item is Not remove",Toast.LENGTH_SHORT).show()
            }
        }

    }
}