package com.example.juniperjunctiondistillery.menu.suppliers

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.juniperjunctiondistillery.adapters.SupplierInvoiceAdapater
import com.example.juniperjunctiondistillery.databinding.ActivitySupplierInvoiceListBinding
import com.example.juniperjunctiondistillery.models.SupplierInvoiceModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SupplierInvoiceListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySupplierInvoiceListBinding
    private lateinit var progressDialog: ProgressDialog

    private var suppCatInvId=""
    private var suppCatInv =""

    private companion object{
        const val TAG = "SUPPLIER_INVOICE_LIST"
    }
    private lateinit var SuppInvArrayList:ArrayList<SupplierInvoiceModel>
    private lateinit var suppInvAdapter:SupplierInvoiceAdapater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupplierInvoiceListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        val intent = intent
        suppCatInvId = intent.getStringExtra("suppCatInvId")!!
        suppCatInv = intent.getStringExtra("suppCatInv")!!

        loadList()

    }

    private fun loadList() {
        SuppInvArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Supplier Invoice")
        ref.orderByChild("Category").equalTo(suppCatInvId)
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    SuppInvArrayList.clear()
                    for(ds in snapshot.children){
                        val model = ds.getValue(SupplierInvoiceModel::class.java)
                        if (model != null) {
                            SuppInvArrayList.add(model)
                            Log.d(TAG, "onDataChange: ID = ${model.ID}, Supplier = ${model.Supplier}")

                        }

                    }
                    suppInvAdapter = SupplierInvoiceAdapater(this@SupplierInvoiceListActivity, SuppInvArrayList)
                    binding.suppInvRv.adapter = suppInvAdapter
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