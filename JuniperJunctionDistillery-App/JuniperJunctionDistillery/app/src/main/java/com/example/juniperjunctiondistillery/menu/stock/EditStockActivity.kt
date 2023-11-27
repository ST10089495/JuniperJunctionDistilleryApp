package com.example.juniperjunctiondistillery.menu.stock

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.databinding.ActivityEditStockBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EditStockActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditStockBinding

    private companion object {
        private const val TAG = "STOCK_EDIT_TAG"
    }

    private var stockId = ""
    private lateinit var progressDialog: ProgressDialog
    private lateinit var categoryTitleArrayList: ArrayList<String>
    private lateinit var categoryIdArrayList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        stockId = intent.getStringExtra("stockId")!!

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        loadCategories()
        loadStockInfo()
        binding.catTv.setOnClickListener {
            categoryDialog()
        }
        binding.arrivEt.setOnClickListener {
            showDatePickerDialog()
        }

        binding.updateBtn.setOnClickListener {
             validateData()
        }
    }

    private fun loadStockInfo() {
        Log.d(TAG, "loadStockInfo: Loading Book Info")

        val ref = FirebaseDatabase.getInstance().getReference("Stock")
        ref.child(stockId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("Name").value.toString()
                    val description = snapshot.child("Description").value.toString()
                    val price = snapshot.child("Price").value.toString()
                    val quantity = snapshot.child("Quantity").value.toString()
                    val arrival = snapshot.child("Arrival").value.toString()
                    selectedCatId = snapshot.child("Category").value.toString()

                    binding.nameEt.setText(name)
                    binding.descrEt.setText(description)
                    binding.priceEt.setText(price)
                    binding.quanEt.setText(quantity)
                    binding.arrivEt.setText(arrival)

                    Log.d(TAG, "onDataChange: Loading Categories")
                    val refStockCat = FirebaseDatabase.getInstance().getReference("Stock Categories")
                    refStockCat.child(selectedCatId)
                        .addListenerForSingleValueEvent(object:ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val category = snapshot.child("category").value

                                binding.catTv.text = category.toString()
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })


                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private var name = ""
    private var descr = ""
    private var price = ""
    private var quan = ""
    private var arriv = ""

    private fun validateData() {
        name = binding.nameEt.text.toString().trim()
        descr = binding.descrEt.text.toString().trim()
        price = binding.priceEt.text.toString().trim()
        quan = binding.quanEt.text.toString().trim()
        arriv = binding.arrivEt.text.toString().trim()

        if(name.isEmpty()){
            Toast.makeText(this,"Enter Name", Toast.LENGTH_SHORT).show()
        }
        else if(descr.isEmpty()){
            Toast.makeText(this,"Enter Description", Toast.LENGTH_SHORT).show()
        }
        else if(price.isEmpty()){
            Toast.makeText(this,"Enter Price", Toast.LENGTH_SHORT).show()
        }
        else if(quan.isEmpty()){
            Toast.makeText(this,"Enter Quantity", Toast.LENGTH_SHORT).show()
        }
        else if(arriv.isEmpty()){
            Toast.makeText(this,"Select Arrival", Toast.LENGTH_SHORT).show()
        }
        else if(selectedCatId.isEmpty()){
            Toast.makeText(this,"Pick Category",Toast.LENGTH_SHORT).show()
        }
        else{
            update()
        }

    }
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth" // Format the date
                binding.arrivEt.setText(selectedDate) // Set the selected date to the EditText
            },
            // Pass the current date as the default selected date
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }
    private fun update() {
        Log.d(TAG, "update: Starting Update...")

        progressDialog.setMessage("Updating")
        progressDialog.show()

        val hashMap = HashMap<String,Any>()
        hashMap["Name"] = name
        hashMap["Description"] = descr
        hashMap["Price"] = price
        hashMap["Quantity"] = quan
        hashMap["Arrival"] = binding.arrivEt.text.toString()
        hashMap["Category"] = selectedCatId
        
        val ref = FirebaseDatabase.getInstance().getReference("Stock")
        ref.child(stockId)
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

    private var selectedCatId = ""
    private var selectedCatTitle = ""

    private fun categoryDialog() {
        val catArray = arrayOfNulls<String>(categoryTitleArrayList.size)
        for (i in categoryTitleArrayList.indices) {
            catArray[i] = categoryTitleArrayList[i]
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Category")
            .setItems(catArray){dialog,position->
                selectedCatId = categoryIdArrayList[position]
                selectedCatTitle = categoryTitleArrayList[position]

                binding.catTv.text = selectedCatTitle
            }
            .show()
    }

    private fun loadCategories() {
        Log.d(TAG, "loadCategories: Loading Categories...")
        categoryTitleArrayList = ArrayList()
        categoryIdArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Stock Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryIdArrayList.clear()
                categoryTitleArrayList.clear()

                for (ds in snapshot.children) {
                    val id = "${ds.child("id").value}"
                    val category = "${ds.child("category").value}"

                    categoryIdArrayList.add(id)
                    categoryTitleArrayList.add(category)

                    Log.d(TAG, "onDataChange: Category ID $id")
                    Log.d(TAG, "onDataChange: Category $category")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
