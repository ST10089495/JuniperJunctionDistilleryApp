package com.example.juniperjunctiondistillery.menu.suppliers

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.databinding.ActivityAddStockCategoryBinding
import com.example.juniperjunctiondistillery.databinding.ActivityAddSuppliersCategoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddSuppliersCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSuppliersCategoryBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding= ActivityAddSuppliersCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)
        binding.suppCatBtn.setOnClickListener {
            validateData()
        }
    }

    private var suppCategory = ""
    private fun validateData() {
        suppCategory = binding.SuppCatNameEt.text.toString().trim()
        if(suppCategory.isEmpty()){
            Toast.makeText(this,"Enter Category...",Toast.LENGTH_SHORT).show()
        }
        else{
            addCategory()
        }

    }

    private fun addCategory() {
        progressDialog.show()

        val timestamp = System.currentTimeMillis()

        val hashMap = HashMap<String,Any>()
        hashMap["SupplierCategoryID"] = "$timestamp"
        hashMap["SupplierCategory"] = suppCategory

        val ref = FirebaseDatabase.getInstance().getReference("Supplier Categories")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Added Category Successfully...",Toast.LENGTH_SHORT).show()
                binding.SuppCatNameEt.text.clear()
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this,"Failed due to ${e.message}",Toast.LENGTH_SHORT).show()

            }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}