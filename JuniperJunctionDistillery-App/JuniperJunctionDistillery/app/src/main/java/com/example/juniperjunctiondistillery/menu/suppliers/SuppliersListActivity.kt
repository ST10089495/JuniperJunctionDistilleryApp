package com.example.juniperjunctiondistillery.menu.suppliers

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.adapters.SupplierAdapter
import com.example.juniperjunctiondistillery.databinding.ActivitySuppliersListBinding
import com.example.juniperjunctiondistillery.models.SupplierModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SuppliersListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuppliersListBinding
    private lateinit var progressDialog: ProgressDialog

    private var supp =""
    private var suppId=""

    private lateinit var suppArrayList:ArrayList<SupplierModel>
    private lateinit var adapterSupplier: SupplierAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivitySuppliersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        val intent = intent
        suppId = intent.getStringExtra("suppId")!!
        supp = intent.getStringExtra("supp")!!

        loadSuppliers()

    }

    private fun loadSuppliers() {
        suppArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Suppliers")
        ref.orderByChild("SupplierCategory").equalTo(suppId)
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    suppArrayList.clear()
                    for(ds in snapshot.children){
                        val model = ds.getValue(SupplierModel::class.java)
                        if (model != null) {
                            suppArrayList.add(model)
                        }
                    }
                    adapterSupplier = SupplierAdapter(this@SuppliersListActivity,suppArrayList)
                    binding.suppRv.adapter = adapterSupplier
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

