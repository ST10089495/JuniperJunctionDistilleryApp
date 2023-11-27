package com.example.juniperjunctiondistillery.menu.stock

import android.app.AlertDialog
import android.app.Application
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.juniperjunctiondistillery.databinding.ActivityAddStockBinding
import com.example.juniperjunctiondistillery.models.CategoryModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class AddStockActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStockBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private lateinit var categoryArrayList: ArrayList<CategoryModel>
    private val TAG = "ADDED"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()
        loadStockCat()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.ArriEt.setOnClickListener {
            showDatePickerDialog()
        }

        binding.categoryTV.setOnClickListener {
            categoryPickDialog()
        }
        binding.stockBtn.setOnClickListener {
            validateData()
        }

    }
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth" // Format the date
                binding.ArriEt.setText(selectedDate) // Set the selected date to the EditText
            },
            // Pass the current date as the default selected date
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private var Name = ""
    private var Description = ""
    private var Price = ""
    private var Quantity = ""
    private var Arrival = ""
    private var Category = ""

    private fun validateData() {
        Log.d(TAG, "validateData: Validating Data")

        Name = binding.NameEt.text.toString().trim()
        Description = binding.DescrEt.text.toString().trim()
        Price = binding.PriceEt.text.toString().trim()
        Quantity = binding.QuanEt.text.toString().trim()
        Arrival = binding.ArriEt.text.toString().trim()
        Category = binding.categoryTV.text.toString().trim()

        if (Name.isEmpty()) {
            Toast.makeText(this, "Enter Name...", Toast.LENGTH_SHORT).show()
        } else if (Description.isEmpty()) {
            Toast.makeText(this, "Enter Description...", Toast.LENGTH_SHORT).show()
        } else if (Price.isEmpty()) {
            Toast.makeText(this, "Enter Price...", Toast.LENGTH_SHORT).show()
        } else if (Quantity.isEmpty()) {
            Toast.makeText(this, "Enter Quantity...", Toast.LENGTH_SHORT).show()
        } else if (Arrival.isEmpty()) {
            Toast.makeText(this, "Enter Arrival...", Toast.LENGTH_SHORT).show()
        } else if (Category.isEmpty()) {
            Toast.makeText(this, "Enter Category...", Toast.LENGTH_SHORT).show()
        }
        else{
            uploadStock()
        }
    }


    private fun uploadStock() {
        Log.d(TAG, "uploadStock: Uploading to Database")
        progressDialog.show()


        val timestamp = System.currentTimeMillis()

        val hashMap : HashMap<String, Any > = HashMap()
        hashMap["ID"] = "$timestamp"
        hashMap["Name"] = Name
        hashMap["Description"] = Description
        hashMap["Price"] = Price
        hashMap["Quantity"] = Quantity
        hashMap["Arrival"] = binding.ArriEt.text.toString()
        hashMap["Category"] = selectedCategoryId



        val ref = FirebaseDatabase.getInstance().getReference("Stock")//this is the table you saving it under
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Added Successfully!!", Toast.LENGTH_SHORT).show()
                binding.NameEt.text.clear()
                binding.DescrEt.text.clear()
                binding.PriceEt.text.clear()
                binding.QuanEt.text.clear()
                binding.ArriEt.text.clear()

            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to add due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun loadStockCat() {
        Log.d(TAG, "LoadStockCat: Loading Stock Categories")

        categoryArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Stock Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for (ds in snapshot.children) {
                    val model = ds.getValue(CategoryModel::class.java)
                    categoryArrayList.add(model!!)
                    Log.d(TAG, "onDataChange: ${model.category}")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private var selectedCategoryId = ""
    private var selectedTitle = ""
    private fun categoryPickDialog() {
        Log.d(TAG, "categoryPickDialog: Showing Stock Category Pick Dialog")
        val categoryArray = arrayOfNulls<String>(categoryArrayList.size)
        for (i in categoryArray.indices) {
            categoryArray[i] = categoryArrayList[i].category
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Categories")
            .setItems(categoryArray) { dialog, which ->
                selectedTitle = categoryArrayList[which].category
                selectedCategoryId = categoryArrayList[which].id

                binding.categoryTV.text = selectedTitle
                Log.d(TAG, "categoryPickDialog: Selected Category ID: $selectedCategoryId")
                Log.d(TAG, "categoryPickDialog: Selected Category Title: $selectedTitle")
            }
            .show()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
