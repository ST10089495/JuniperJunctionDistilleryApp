package com.example.juniperjunctiondistillery.menu.suppliers

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.juniperjunctiondistillery.databinding.ActivityEditSupplierInvoiceBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EditSupplierInvoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSupplierInvoiceBinding

    private var suppInvId = ""

    private companion object {
        private const val TAG = "INVOICE_EDIT_TAG"
    }

    private lateinit var categoryTitleArrayList: ArrayList<String>
    private lateinit var categoryIdArrayList: ArrayList<String>

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSupplierInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        suppInvId = intent.getStringExtra("suppInvId")!!

        loadCategories()
        loadInvoiceInfo()
        binding.catTv.setOnClickListener {
            categoryDialog()
        }
        binding.disDateEt.setOnClickListener {
            showDatePickerDialog()
        }
        binding.delivDateEt.setOnClickListener {
            showDatePickerDialog1()
        }
        binding.updateBtn.setOnClickListener {
            validateData()
        }
    }


    private var Supplier = ""
    private var Category = ""
    private var DeliveryNotes = ""
    private var TotalPrice = ""
    private var DispatchDate = ""
    private var DeliveryDate = ""
    private fun validateData() {
        Log.d(TAG, "validateData: Validating Data")
        Supplier = binding.suppTv.text.toString().trim()
        Category = binding.catTv.text.toString().trim()
        DeliveryNotes = binding.delivNotesEt.text.toString().trim()
        TotalPrice = binding.priceTotSuppEt.text.toString().trim()
        DispatchDate = binding.disDateEt.text.toString().trim()
        DeliveryDate = binding.delivDateEt.text.toString().trim()

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
        } else {
            updateInvoice()
        }
    }

    private fun updateInvoice() {
        Log.d(TAG, "updateInvoice: Updating...")

        progressDialog.setMessage("Updating")
        progressDialog.show()

        val hashMap = HashMap<String,Any>()
        hashMap["Supplier"] =Supplier
        hashMap["Category"] = selectedCatId
        hashMap["DeliveryNotes"] = DeliveryNotes
        hashMap["TotalPrice"] = TotalPrice
        hashMap["DispatchDate"] = binding.disDateEt.text.toString()
        hashMap["DeliveryDate"] = binding.delivDateEt.text.toString()

        val ref = FirebaseDatabase.getInstance().getReference("Supplier Invoice")
        ref.child(suppInvId)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Updated Successfully...", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failure Due To ${e.message}...", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDatePickerDialog1() {
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth" // Format the date
                binding.delivDateEt.setText(selectedDate) // Set the selected date to the EditText
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
                binding.disDateEt.setText(selectedDate) // Set the selected date to the EditText
            },
            // Pass the current date as the default selected date
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun loadInvoiceInfo() {
        Log.d(TAG, "loadInvoiceInfo: Loading...")

        val ref = FirebaseDatabase.getInstance().getReference("Supplier Invoice")
        ref.child(suppInvId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    selectedCatId = snapshot.child("Category").value.toString()
                    val supp = snapshot.child("Supplier").value.toString()
                    val delivNotes = snapshot.child("DeliveryNotes").value.toString()
                    val totpri = snapshot.child("TotalPrice").value.toString()
                    val disdate = snapshot.child("DispatchDate").value.toString()
                    val delivdate = snapshot.child("DeliveryDate").value.toString()

                    binding.suppTv.setText(supp)
                    binding.delivNotesEt.setText(delivNotes)
                    binding.priceTotSuppEt.setText(totpri)
                    binding.disDateEt.setText(disdate)
                    binding.delivDateEt.setText(delivdate)
                    Log.d(TAG, "onDataChange: Loading Categories")
                    val refStockCat =
                        FirebaseDatabase.getInstance().getReference("Supplier Category")
                    refStockCat.child(selectedCatId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val category = snapshot.child("SupplierCategoryInvoice").value

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

    private var selectedCatId = ""
    private var selectedCatTitle = ""
    private fun categoryDialog() {
        val catArray = arrayOfNulls<String>(categoryTitleArrayList.size)
        for (i in categoryTitleArrayList.indices) {
            catArray[i] = categoryTitleArrayList[i]
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Category:")
            .setItems(catArray) { dialog, position ->
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

        val ref = FirebaseDatabase.getInstance().getReference("Supplier Category")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryIdArrayList.clear()
                categoryTitleArrayList.clear()

                for (ds in snapshot.children) {
                    val id = "${ds.child("SupplierCategoryInvoiceID").value}"
                    val category = "${ds.child("SupplierCategoryInvoice").value}"

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
}