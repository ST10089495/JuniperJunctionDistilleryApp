package com.example.juniperjunctiondistillery.menu.employee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.juniperjunctiondistillery.databinding.ActivityEmployeeDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EmployeeDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeDetailsBinding
    private var empId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        empId = intent.getStringExtra("empId")!!

        loadEmpDetails()
    }

    private fun loadEmpDetails() {

        val stockRef = FirebaseDatabase.getInstance().getReference("Employees").child(empId)

        stockRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                        val fname = "${snapshot.child("FirstName").value}"
                        val sname = "${snapshot.child("Surname").value}"
                        val cnum = "${snapshot.child("ContactNumber").value}"
                        val addre = "${snapshot.child("Address").value}"
                        val email = "${snapshot.child("Email").value}"
                        val mana = "${snapshot.child("Manager").value}"

                        // Set the data in your UI components
                        binding.fnameTV.text = fname
                        binding.NameTV.text = sname
                        binding.cnumTV.text = cnum
                        binding.addreTV.text = addre
                        binding.emailTV.text = email
                        binding.manaTV.text = mana
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