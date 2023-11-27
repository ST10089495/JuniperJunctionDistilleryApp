package com.example.juniperjunctiondistillery.menu.suppliers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.adapters.SupplierCategoryAdapter
import com.example.juniperjunctiondistillery.adapters.SupplierCategoryInvoiceAdapter
import com.example.juniperjunctiondistillery.databinding.ActivitySuppliersBinding
import com.example.juniperjunctiondistillery.menu.stock.AddStockActivity
import com.example.juniperjunctiondistillery.menu.stock.AddStockCategoryActivity
import com.example.juniperjunctiondistillery.models.SupplierCategoryModel
import com.example.juniperjunctiondistillery.models.SupplierInvoiceCategoryModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SuppliersActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuppliersBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private val rotateOpen : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
    private val rotateClose : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
    private val fromBottom : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottom : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}

    private var clicked = false

    private lateinit var suppCategoryArrayList:ArrayList<SupplierCategoryModel>
    private lateinit var suppCategoryAdapter:SupplierCategoryAdapter

    private lateinit var suppInvCategoryArrayList:ArrayList<SupplierInvoiceCategoryModel>
    private lateinit var suppInvCategoryAdapter:SupplierCategoryInvoiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuppliersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()
        loadSuppCat()
        loadSuppInvCat()

        binding.searchSuppEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    suppCategoryAdapter.filter.filter(s)
                }
                catch (e:java.lang.Exception){

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        val btn = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        btn.setOnClickListener{
            onAddButtonClicked()
        }
        val addBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        addBtn.setOnClickListener {
            val intent = Intent(this, AddSupplierActivity::class.java)
            startActivity(intent)
        }
        val addCatBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton3)
        addCatBtn.setOnClickListener {
            val intent = Intent(this, AddSupplierInvoiceActivity::class.java)
            startActivity(intent)
        }
        val addCatBtn1 = findViewById<FloatingActionButton>(R.id.floatingActionButton4)
        addCatBtn1.setOnClickListener {
            val intent = Intent(this, SupplierInvoiceCategoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadSuppInvCat() {
        suppInvCategoryArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Supplier Category")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                suppInvCategoryArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(SupplierInvoiceCategoryModel::class.java)
                    suppInvCategoryArrayList.add(model!!)
                }
                suppInvCategoryAdapter = SupplierCategoryInvoiceAdapter(this@SuppliersActivity,suppInvCategoryArrayList)
                binding.SuppRvCat1.adapter = suppInvCategoryAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun loadSuppCat() {
        suppCategoryArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Supplier Categories")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                suppCategoryArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(SupplierCategoryModel::class.java)
                    suppCategoryArrayList.add(model!!)
                }
                suppCategoryAdapter = SupplierCategoryAdapter(this@SuppliersActivity,suppCategoryArrayList)
                binding.SuppRvCat.adapter = suppCategoryAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun onAddButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked : Boolean) {
        val btn = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        val addBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        val addCatBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton3)
        val addCatBtn1 = findViewById<FloatingActionButton>(R.id.floatingActionButton4)

        if (!clicked){
            addBtn.startAnimation(fromBottom)
            addCatBtn.startAnimation(fromBottom)
            addCatBtn1.startAnimation(fromBottom)
            btn.startAnimation(rotateOpen)
        }else{
            addBtn.startAnimation(toBottom)
            addCatBtn.startAnimation(toBottom)
            addCatBtn1.startAnimation(toBottom)
            btn.startAnimation(rotateClose)
        }

    }

    private fun setVisibility(clicked : Boolean) {
        val addBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        val addCatBtn = findViewById<FloatingActionButton>(R.id.floatingActionButton3)
        val addCatBtn1 = findViewById<FloatingActionButton>(R.id.floatingActionButton4)

        if(!clicked){
            addBtn.visibility = View.VISIBLE
            addCatBtn.visibility = View.VISIBLE
            addCatBtn1.visibility = View.VISIBLE

        }
        else{
            addBtn.visibility = View.INVISIBLE
            addCatBtn.visibility = View.INVISIBLE
            addCatBtn1.visibility = View.VISIBLE

        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}