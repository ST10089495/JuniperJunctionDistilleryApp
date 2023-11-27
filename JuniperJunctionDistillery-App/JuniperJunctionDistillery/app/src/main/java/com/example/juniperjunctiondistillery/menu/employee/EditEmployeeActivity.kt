package com.example.juniperjunctiondistillery.menu.employee

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.databinding.ActivityEditEmployeeBinding
import com.example.juniperjunctiondistillery.menu.stock.EditStockActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditEmployeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditEmployeeBinding
    private companion object {
        private const val TAG = "EMP_EDIT_TAG"
    }
    private var empId = ""
    private lateinit var progressDialog: ProgressDialog
    private lateinit var employeeNameArrayList: ArrayList<String>
    private lateinit var employeeIdArrayList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        empId = intent.getStringExtra("empId")!!

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        loadEmpInfo()
        binding.empUpdateBtn.setOnClickListener {
            validateData()
        }
    }
    private fun loadEmpInfo() {
        Log.d(TAG, "loadEmpInfo: Loading Employee Info")

        val ref = FirebaseDatabase.getInstance().getReference("Employees")
        ref.child(empId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val fname = snapshot.child("FirstName").value.toString()
                    val sname = snapshot.child("Surname").value.toString()
                    val cnum = snapshot.child("ContactNumber").value.toString()
                    val addre = snapshot.child("Address").value.toString()
                    val email = snapshot.child("Email").value.toString()
                    val mana = snapshot.child("Manager").value.toString()

                    binding.FnameEt.setText(fname)
                    binding.sNameEt.setText(sname)
                    binding.cNumEt.setText(cnum)
                    binding.AddreEt.setText(addre)
                    binding.emaEt.setText(email)
                    binding.manaEt.setText(mana)

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
    private var fname = ""
    private var sname = ""
    private var cnum = ""
    private var addre = ""
    private var email = ""
    private var mana = ""

    private fun validateData() {
        fname = binding.FnameEt.text.toString().trim()
        sname = binding.sNameEt.text.toString().trim()
        cnum = binding.cNumEt.text.toString().trim()
        addre = binding.AddreEt.text.toString().trim()
        email = binding.emaEt.text.toString().trim()
        mana = binding.manaEt.text.toString().trim()

        if (fname.isEmpty()) {
            Toast.makeText(this, "Enter First Name...", Toast.LENGTH_SHORT).show()
        } else if (sname.isEmpty()) {
            Toast.makeText(this, "Enter Surname...", Toast.LENGTH_SHORT).show()
        } else if (cnum.isEmpty()) {
            Toast.makeText(this, "Enter Contact Number...", Toast.LENGTH_SHORT).show()
        } else if (addre.isEmpty()) {
            Toast.makeText(this, "Enter Address...", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Enter Email...", Toast.LENGTH_SHORT).show()
        } else if (mana.isEmpty()) {
            Toast.makeText(this, "Enter Manager...", Toast.LENGTH_SHORT).show()
        }
        else{
            update()
        }
    }

    private fun update() {
        Log.d(TAG, "update: Starting Update...")

        progressDialog.setMessage("Updating")
        progressDialog.show()

        val hashMap = HashMap<String,Any>()
        hashMap["FirstName"] = fname
        hashMap["Surname"] = sname
        hashMap["ContactNumber"] = cnum
        hashMap["Address"] = addre
        hashMap["Email"] = email
        hashMap["Manager"] = mana

        val ref = FirebaseDatabase.getInstance().getReference("Employees")//this is the table you saving it under
        ref.child(empId)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "update: Updated Successfully...")
                progressDialog.dismiss()
                Toast.makeText(this,"Updated Successfully...",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Log.d(TAG, "update: Failed to update due to${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this,"Failed to update due to${e.message}",Toast.LENGTH_SHORT).show()
            }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}