package com.example.juniperjunctiondistillery.menu.suppliers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.juniperjunctiondistillery.databinding.ActivitySupplierInvoiceDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SupplierInvoiceDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySupplierInvoiceDetailsBinding
    private var invId=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySupplierInvoiceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        invId = intent.getStringExtra("invId")!!

        loadDetails()
    }

    private fun loadDetails() {
        val stockRef = FirebaseDatabase.getInstance().getReference("Supplier Invoice").child(invId)

        stockRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryID = "${snapshot.child("Category").value}"

                // Fetch the category name based on the categoryID
                val categoryRef = FirebaseDatabase.getInstance().getReference("Supplier Category")
                    .child(categoryID)
                categoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(categorySnapshot: DataSnapshot) {
                        val categoryName = "${categorySnapshot.child("SupplierCategoryInvoice").value}"

                        // Fetch other details like Name, Description, etc.
                        val invname = "${snapshot.child("Supplier").value}"
                        val delinotes = "${snapshot.child("DeliveryNotes").value}"
                        val price = "${snapshot.child("TotalPrice").value}"
                        val dispdate = "${snapshot.child("DispatchDate").value}"
                        val delivdate = "${snapshot.child("DeliveryDate").value}"


                        // Set the data in your UI components
                        binding.catInvTV.text = categoryName
                        binding.NameInvTV.text = invname
                        binding.invDelNotTV.text = delinotes
                        binding.totPriTV.text = price
                        binding.disDateTV.text = dispdate
                        binding.deliDateTV.text=delivdate
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
}