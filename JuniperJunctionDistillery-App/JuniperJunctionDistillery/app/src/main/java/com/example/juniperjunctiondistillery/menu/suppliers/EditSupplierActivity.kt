package com.example.juniperjunctiondistillery.menu.suppliers

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.databinding.ActivityEditSupplierBinding
import com.example.juniperjunctiondistillery.menu.stock.EditStockActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditSupplierActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSupplierBinding

    private companion object {
        private const val TAG = "SUPPLIER_EDIT_TAG"
    }

    private var suppId = ""
    private lateinit var progressDialog: ProgressDialog
    private lateinit var categoryTitleArrayList: ArrayList<String>
    private lateinit var categoryIdArrayList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSupplierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        suppId = intent.getStringExtra("suppId")!!

        loadSupplierInfo()
        loadCategories()
        binding.catTv.setOnClickListener {
            categoryDialog()
        }
        binding.updateBtn.setOnClickListener {
            validateData()
        }
    }

    private fun loadSupplierInfo() {
        Log.d(TAG, "loadSupplierInfo: Loading Supplier Info")

        val ref = FirebaseDatabase.getInstance().getReference("Suppliers")
        ref.child(suppId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                selectedCatId = snapshot.child("SupplierCategory").value.toString()
                val description = snapshot.child("SupplierDescription").value.toString()
                val name = snapshot.child("SupplierName").value.toString()
                val conNum = snapshot.child("SupplierContactNumber").value.toString()
                val email = snapshot.child("SupplierEmail").value.toString()

                binding.nameSuppEt.setText(name)
                binding.descrSuppEt.setText(description)
                binding.conNumSuppEt.setText(conNum)
                binding.emailSuppEt.setText(email)
                Log.d(TAG, "onDataChange: Loading Categories")
                val refStockCat = FirebaseDatabase.getInstance().getReference("Supplier Categories")
                refStockCat.child(selectedCatId)
                    .addListenerForSingleValueEvent(object:ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val category = snapshot.child("SupplierCategory").value

                            binding.catTv.text = category.toString()
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
    private var suppName =""
    private var suppDescr =""
    private var suppConNum=""
    private var suppEmail =""
    private var category =""
    private fun validateData() {
        Log.d(TAG, "validateData: Validating Data")

        suppName = binding.nameSuppEt.text.toString().trim()
        suppDescr = binding.descrSuppEt.text.toString().trim()
        suppConNum = binding.conNumSuppEt.text.toString().trim()
        suppEmail = binding.emailSuppEt.text.toString().trim()
        category = binding.catTv.text.toString().trim()

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
            update()
        }
    }

    private fun update() {
        Log.d(TAG, "update: Starting Update...")

        progressDialog.setMessage("Updating")
        progressDialog.show()

        val hashMap: HashMap<String,Any> = HashMap()
        hashMap["SupplierName"] = suppName
        hashMap["SupplierDescription"] = suppDescr
        hashMap["SupplierContactNumber"] = suppConNum
        hashMap["SupplierEmail"] = suppEmail
        hashMap["SupplierCategory"] = selectedCatId

        val ref = FirebaseDatabase.getInstance().getReference("Suppliers")
        ref.child(suppId)
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

        val ref = FirebaseDatabase.getInstance().getReference("Supplier Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryIdArrayList.clear()
                categoryTitleArrayList.clear()

                for (ds in snapshot.children) {
                    val id = "${ds.child("SupplierCategoryID").value}"
                    val category = "${ds.child("SupplierCategory").value}"

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
