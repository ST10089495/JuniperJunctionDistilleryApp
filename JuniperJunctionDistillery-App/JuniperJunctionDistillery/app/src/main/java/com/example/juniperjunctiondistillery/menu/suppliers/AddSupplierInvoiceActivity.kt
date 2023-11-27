package com.example.juniperjunctiondistillery.menu.suppliers

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.databinding.ActivityAddSupplierInvoiceBinding
import com.example.juniperjunctiondistillery.models.SupplierCategoryModel
import com.example.juniperjunctiondistillery.models.SupplierInvoiceCategoryModel
import com.example.juniperjunctiondistillery.models.SupplierModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class AddSupplierInvoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSupplierInvoiceBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private lateinit var catInvArrayList:ArrayList<SupplierInvoiceCategoryModel>
    private lateinit var suppArrayList:ArrayList<SupplierModel>


    private val TAG = "SUPPLIER_INVOICE_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSupplierInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        loadCategories()
        loadSuppliers()

        binding.SuppCatTV.setOnClickListener {
            catPickDialog()
        }
        binding.SuppTV.setOnClickListener {
            suppPickDialog()
        }
        binding.SuppDispatchEt.setOnClickListener {
            showDatePickerDialog1()
        }
        binding.SuppDelivEt.setOnClickListener {
            showDatePickerDialog()
        }
        binding.addSuppInvBtn.setOnClickListener {
            validateData()
        }

    }



    private fun loadSuppliers() {
        Log.d(TAG, "loadSuppliers: Loading...")
        suppArrayList= ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Suppliers")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                suppArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(SupplierModel::class.java)
                    suppArrayList.add(model!!)
                    Log.d(TAG, "onDataChange: ${model.SupplierName}")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun loadCategories() {
        Log.d(TAG, "loadCategories: Loading...")
        catInvArrayList= ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Supplier Category")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                catInvArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(SupplierInvoiceCategoryModel::class.java)
                    catInvArrayList.add(model!!)
                    Log.d(TAG, "onDataChange: ${model.SupplierCategoryInvoice}")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
    private var selectedSuppId=""
    private var selectedSupp=""
    private fun suppPickDialog() {
        Log.d(TAG, "suppPickDialog: Showing Dialog...")

        val suppArray = arrayOfNulls<String>(suppArrayList.size)
        for (i in suppArrayList.indices){
            suppArray[i] = suppArrayList[i].SupplierName
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Supplier")
            .setItems(suppArray){dialog,which->
                selectedSupp = suppArrayList[which].SupplierName
                selectedSuppId = suppArrayList[which].ID


                binding.SuppTV.text=selectedSupp
                Log.d(TAG, "suppPickDialog: Selected Supplier ID:$selectedSuppId")
                Log.d(TAG, "suppPickDialog: Selected Supplier:$selectedSupp")
            }
            .show()
    }
    private var selectedCatId=""
    private var selectedCat=""
    private fun catPickDialog(){
        Log.d(TAG, "catPickDialog: Showing Dialog")
        val categoryInvArray = arrayOfNulls<String>(catInvArrayList.size)
        for (i in catInvArrayList.indices){
            categoryInvArray[i] = catInvArrayList[i].SupplierCategoryInvoice
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Category")
            .setItems(categoryInvArray){dialog,which->
                selectedCat = catInvArrayList[which].SupplierCategoryInvoice
                selectedCatId = catInvArrayList[which].SupplierCategoryInvoiceID

                binding.SuppCatTV.text = selectedCat
                Log.d(TAG, "catPickDialog: Selected Category ID:$selectedCatId")
                Log.d(TAG, "catPickDialog: Selected Category:$selectedCat")
            }
            .show()
    }


    private fun showDatePickerDialog1() {
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth" // Format the date
                binding.SuppDispatchEt.setText(selectedDate) // Set the selected date to the EditText
            },
            // Pass the current date as the default selected date
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth" // Format the date
                binding.SuppDelivEt.setText(selectedDate) // Set the selected date to the EditText
            },
            // Pass the current date as the default selected date
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private var Supplier = ""
    private var Category = ""
    private var DeliveryNotes = ""
    private var TotalPrice = ""
    private var DispatchDate = ""
    private var DeliveryDate = ""
    private fun validateData() {
        Log.d(TAG, "validateData: Validating Data")
        Supplier = binding.SuppTV.text.toString().trim()
        Category = binding.SuppCatTV.text.toString().trim()
        DeliveryNotes = binding.SuppDelivNotesEt.text.toString().trim()
        TotalPrice = binding.SuppPriceEt.text.toString().trim()
        DispatchDate = binding.SuppDispatchEt.text.toString().trim()
        DeliveryDate = binding.SuppDelivEt.text.toString().trim()

        if (Supplier.isEmpty()) {
            Toast.makeText(this, "Pick Supplier...", Toast.LENGTH_SHORT).show()
        } else if (Category.isEmpty()) {
            Toast.makeText(this, "Pick Category...", Toast.LENGTH_SHORT).show()
        } else if (DeliveryNotes.isEmpty()) {
            Toast.makeText(this, "Enter Delivery Notes...", Toast.LENGTH_SHORT).show()
        } else if (TotalPrice.isEmpty()) {
            Toast.makeText(this, "Enter Price...", Toast.LENGTH_SHORT).show()
        } else if (DispatchDate.isEmpty()) {
            Toast.makeText(this, "Pick Dispatch Date...", Toast.LENGTH_SHORT).show()
        } else if (DeliveryDate.isEmpty()) {
            Toast.makeText(this, "Enter Delivery Date...", Toast.LENGTH_SHORT).show()
        }
        else{
            uploadInvoice()
        }
    }

    private fun uploadInvoice() {
        Log.d(TAG, "uploadStock: Uploading to Database")
        progressDialog.show()


        val timestamp = System.currentTimeMillis()

        val hashMap : HashMap<String, Any > = HashMap()
        hashMap["ID"] = "$timestamp"
        hashMap["Supplier"] = selectedSupp
        hashMap["Category"] = selectedCatId
        hashMap["DeliveryNotes"] = DeliveryNotes
        hashMap["TotalPrice"] = TotalPrice
        hashMap["DispatchDate"] = binding.SuppDispatchEt.text.toString()
        hashMap["DeliveryDate"] = binding.SuppDelivEt.text.toString()

        val ref = FirebaseDatabase.getInstance().getReference("Supplier Invoice")//this is the table you saving it under
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Added Successfully!!", Toast.LENGTH_SHORT).show()
                binding.SuppDelivNotesEt.text.clear()
                binding.SuppPriceEt.text.clear()
                binding.SuppDelivEt.text.clear()
                binding.SuppDispatchEt.text.clear()

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
