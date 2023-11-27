package com.example.juniperjunctiondistillery.menu.employee

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.databinding.ActivityAddEmployeeBinding
import com.example.juniperjunctiondistillery.databinding.ActivityAddStockBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEmployeeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private val TAG = "ADDED"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEmployeeBinding .inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.empBtn.setOnClickListener {
            validateData()
        }
    }

    private var FName = ""
    private var SName = ""
    private var CNumber = ""
    private var Address = ""
    private var Email = ""
    private var Manager = ""

    private fun validateData() {
        Log.d(TAG, "validateData: Validating Data")

        FName = binding.FNameEt.text.toString().trim()
        SName = binding.SNameEt.text.toString().trim()
        CNumber = binding.CNEt.text.toString().trim()
        Address = binding.AddrEt.text.toString().trim()
        Email = binding.emailEt.text.toString().trim()
        Manager = binding.ManaEt.text.toString().trim()

        if (FName.isEmpty()) {
            Toast.makeText(this, "Enter First Name...", Toast.LENGTH_SHORT).show()
        } else if (SName.isEmpty()) {
            Toast.makeText(this, "Enter Surname...", Toast.LENGTH_SHORT).show()
        } else if (CNumber.isEmpty()) {
            Toast.makeText(this, "Enter Contact Number...", Toast.LENGTH_SHORT).show()
        } else if (Address.isEmpty()) {
            Toast.makeText(this, "Enter Address...", Toast.LENGTH_SHORT).show()
        } else if (Email.isEmpty()) {
            Toast.makeText(this, "Enter Email...", Toast.LENGTH_SHORT).show()
        } else if (Manager.isEmpty()) {
            Toast.makeText(this, "Enter Manager...", Toast.LENGTH_SHORT).show()
        }
        else{
            uploadEmployee()
        }
    }

    private fun uploadEmployee() {
        Log.d(TAG, "uploadEmployee: Uploading to Database")

        val timestamp = System.currentTimeMillis()

        val hashMap : HashMap<String, Any > = HashMap()
        hashMap["ID"] = "$timestamp"
        hashMap["FirstName"] = FName
        hashMap["Surname"] = SName
        hashMap["ContactNumber"] = CNumber
        hashMap["Address"] = Address
        hashMap["Email"] = Email
        hashMap["Manager"] = Manager

        val ref = FirebaseDatabase.getInstance().getReference("Employees")//this is the table you saving it under
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Added Successfully!!", Toast.LENGTH_SHORT).show()
                binding.FNameEt.text.clear()
                binding.SNameEt.text.clear()
                binding.CNEt.text.clear()
                binding.AddrEt.text.clear()
                binding.emailEt.text.clear()
                binding.ManaEt.text.clear()

            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to add due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}