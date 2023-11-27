package com.example.juniperjunctiondistillery.menu.stock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.EventLogTags.Description
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.databinding.ActivityStockDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale.Category

class StockDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockDetailsBinding

    private var stockId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        stockId = intent.getStringExtra("stockId")!!

        loadStockDetails()
    }

    private fun loadStockDetails() {
        val stockRef = FirebaseDatabase.getInstance().getReference("Stock").child(stockId)

        stockRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryID = "${snapshot.child("Category").value}"

                // Fetch the category name based on the categoryID
                val categoryRef = FirebaseDatabase.getInstance().getReference("Stock Categories")
                    .child(categoryID)
                categoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(categorySnapshot: DataSnapshot) {
                        val categoryName = "${categorySnapshot.child("category").value}"

                        // Fetch other details like Name, Description, etc.
                        val name = "${snapshot.child("Name").value}"
                        val description = "${snapshot.child("Description").value}"
                        val price = "${snapshot.child("Price").value}"
                        val quantity = "${snapshot.child("Quantity").value}"
                        val arrival = "${snapshot.child("Arrival").value}"

                        // Set the data in your UI components
                        binding.catTV.text = categoryName
                        binding.NameTV.text = name
                        binding.descrTV.text = description
                        binding.priceTV.text = price
                        binding.quanTV.text = quantity
                        binding.arrivTV.text = arrival
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