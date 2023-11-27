package com.example.juniperjunctiondistillery.menu.suppliers

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.databinding.ActivityAddSupplierBinding
import com.example.juniperjunctiondistillery.models.StockModel
import com.example.juniperjunctiondistillery.models.SupplierCategoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddSupplierActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddSupplierBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private lateinit var suppCategoryArrayList:ArrayList<SupplierCategoryModel>

    private val TAG = "SUPPLIER_ADD_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityAddSupplierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        loadSuppCat()

        binding.SuppCategoryTV.setOnClickListener {
            suppCategory()
        }
        binding.addSuppBtn.setOnClickListener {
            validateData()
        }
    }

    private var suppName =""
    private var suppDescr =""
    private var suppConNum=""
    private var suppEmail =""
    private var category =""
    private fun validateData() {
        Log.d(TAG, "validateData: Validating Data")

        suppName = binding.SuppNameEt.text.toString().trim()
        suppDescr = binding.SuppDescrEt.text.toString().trim()
        suppConNum = binding.SuppConNumEt.text.toString().trim()
        suppEmail = binding.SuppEmailEt.text.toString().trim()
        category = binding.SuppCategoryTV.text.toString().trim()

        if (suppEmail.isEmpty()) {
            Toast.makeText(this, "Enter Supplier Name...", Toast.LENGTH_SHORT).show()
        } else if (suppDescr.isEmpty()) {
            Toast.makeText(this, "Enter Supplier Description...", Toast.LENGTH_SHORT).show()
        } else if (suppConNum.isEmpty()) {
            Toast.makeText(this, "Enter Supplier Contact Number...", Toast.LENGTH_SHORT).show()
        } else if (suppEmail.isEmpty()) {
            Toast.makeText(this, "Enter Supplier Email...", Toast.LENGTH_SHORT).show()
        } else if (category.isEmpty()) {
            Toast.makeText(this, "Pick Category...", Toast.LENGTH_SHORT).show()
        }
        else{
            uploadSupplier()
        }
    }

    private fun uploadSupplier() {
        Log.d(TAG, "uploadSupplier: Uploading...")

        progressDialog.setMessage("Uploading...")
        progressDialog.show()

        val timestamp = System.currentTimeMillis()
        val hashMap: HashMap<String,Any> = HashMap()
        hashMap["ID"] ="$timestamp"
        hashMap["SupplierName"] = suppName
        hashMap["SupplierDescription"] = suppDescr
        hashMap["SupplierContactNumber"] = suppConNum
        hashMap["SupplierEmail"] = suppEmail
        hashMap["SupplierCategory"] = selectedSuppCatId

        val ref = FirebaseDatabase.getInstance().getReference("Suppliers")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                Log.d(TAG, "uploadSupplier: Uploaded")
                progressDialog.dismiss()
                Toast.makeText(this, "Successfully Uploaded!!!",Toast.LENGTH_SHORT).show()
                binding.SuppNameEt.text.clear()
                binding.SuppDescrEt.text.clear()
                binding.SuppConNumEt.text.clear()
                binding.SuppEmailEt.text.clear()

            }
            .addOnFailureListener {e->
                Log.d(TAG, "uploadSupplier: Failed due to ${e.message}")
                progressDialog.dismiss()
                Toast.makeText(this,"Failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }

    }

    private fun loadSuppCat() {
        Log.d(TAG, "loadSuppCat: Loading Categories")
        suppCategoryArrayList = ArrayList()
        val ref =FirebaseDatabase.getInstance().getReference("Supplier Categories")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                suppCategoryArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(SupplierCategoryModel::class.java)
                    suppCategoryArrayList.add(model!!)
                    Log.d(TAG, "onDataChange: ${model.SupplierCategory}")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private var selectedSuppCatId=""
    private var selectedSuppCatTitle =""
    private fun suppCategory(){
        Log.d(TAG, "suppCategory: Showing Categories")
        val suppliersArray = arrayOfNulls<String>(suppCategoryArrayList.size)
        for(i in suppCategoryArrayList.indices){
            suppliersArray[i] = suppCategoryArrayList[i].SupplierCategory
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Categories")
            .setItems(suppliersArray){dialog, which->
                selectedSuppCatTitle =suppCategoryArrayList[which].SupplierCategory
                selectedSuppCatId =suppCategoryArrayList[which].SupplierCategoryID

                binding.SuppCategoryTV.text = selectedSuppCatTitle
                Log.d(TAG, "suppCategory: Selected Category ID: $selectedSuppCatId")
                Log.d(TAG, "suppCategory: Selected Category Name: $selectedSuppCatTitle")

            }
            .show()
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}