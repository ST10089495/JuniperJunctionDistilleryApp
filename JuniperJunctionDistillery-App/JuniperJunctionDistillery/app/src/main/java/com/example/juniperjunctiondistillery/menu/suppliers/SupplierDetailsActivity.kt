package com.example.juniperjunctiondistillery.menu.suppliers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.juniperjunctiondistillery.databinding.ActivitySupplierDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SupplierDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupplierDetailsBinding

    private var suppId=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupplierDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        suppId = intent.getStringExtra("suppId")!!
        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        loadDetails()
    }

    private fun loadDetails() {
        val stockRef = FirebaseDatabase.getInstance().getReference("Suppliers").child(suppId)

        stockRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryID = "${snapshot.child("SupplierCategory").value}"

                // Fetch the category name based on the categoryID
                val categoryRef = FirebaseDatabase.getInstance().getReference("Supplier Categories")
                    .child(categoryID)
                categoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(categorySnapshot: DataSnapshot) {
                        val categoryName = "${categorySnapshot.child("SupplierCategory").value}"

                        // Fetch other details like Name, Description, etc.
                        val name = "${snapshot.child("SupplierName").value}"
                        val description = "${snapshot.child("SupplierDescription").value}"
                        val connum = "${snapshot.child("SupplierContactNumber").value}"
                        val email = "${snapshot.child("SupplierEmail").value}"


                        // Set the data in your UI components
                        binding.catSuppTV.text = categoryName
                        binding.suppNameTV.text = name
                        binding.descrSuppTV.text = description
                        binding.conNumSuppTV.text = connum
                        binding.emailSuppTV.text = email
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle errors
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}