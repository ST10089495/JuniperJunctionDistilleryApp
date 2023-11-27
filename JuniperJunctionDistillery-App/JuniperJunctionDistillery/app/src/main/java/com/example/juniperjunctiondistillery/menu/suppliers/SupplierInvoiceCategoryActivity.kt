package com.example.juniperjunctiondistillery.menu.suppliers

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.juniperjunctiondistillery.databinding.ActivitySupplierInvoiceCategoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SupplierInvoiceCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupplierInvoiceCategoryBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupplierInvoiceCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)
        binding.suppInvCatBtn.setOnClickListener {
            validateData()
        }
    }

    private var suppInvCategory = ""
    private fun validateData() {
        suppInvCategory = binding.SuppInvCatNameEt.text.toString().trim()
        if (suppInvCategory.isEmpty()) {
            Toast.makeText(this, "Enter Category...", Toast.LENGTH_SHORT).show()
        } else {
            addCategory()
        }

    }

    private fun addCategory() {
        progressDialog.show()

        val timestamp = System.currentTimeMillis()

        val hashMap = HashMap<String, Any>()
        hashMap["SupplierCategoryInvoiceID"] = "$timestamp"
        hashMap["SupplierCategoryInvoice"] = suppInvCategory

        val ref = FirebaseDatabase.getInstance().getReference("Supplier Category")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Added Category Successfully...", Toast.LENGTH_SHORT).show()
                binding.SuppInvCatNameEt.text.clear()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}