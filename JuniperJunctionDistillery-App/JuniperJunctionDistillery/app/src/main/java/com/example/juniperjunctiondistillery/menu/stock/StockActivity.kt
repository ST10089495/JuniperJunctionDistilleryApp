package com.example.juniperjunctiondistillery.menu.stock

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.adapters.CategoryAdapter
import com.example.juniperjunctiondistillery.databinding.ActivityStockBinding
import com.example.juniperjunctiondistillery.models.CategoryModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StockActivity : AppCompatActivity() {

    private val rotateOpen : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
    private val rotateClose : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
    private val fromBottom : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottom : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}

    private var clicked = false

    private lateinit var binding: ActivityStockBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var categoryArrayList: ArrayList<CategoryModel>
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()
        loadCategories()

        binding.searchEt.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try{
                    categoryAdapter.filter.filter(s)
                }
                catch (e : Exception){

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        val btn = findViewById<FloatingActionButton>(R.id.st_floatingActionButton)
        btn.setOnClickListener{
            onAddButtonClicked()
        }
        val addBtn = findViewById<FloatingActionButton>(R.id.st_floatingActionButton2)
        addBtn.setOnClickListener {
            val intent = Intent(this, AddStockActivity::class.java)
            startActivity(intent)
        }
        val addCatBtn = findViewById<FloatingActionButton>(R.id.st_floatingActionButton3)
        addCatBtn.setOnClickListener {
            val intent = Intent(this, AddStockCategoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadCategories() {
        categoryArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Stock Categories")
        ref.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoryArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(CategoryModel::class.java)
                    categoryArrayList.add(model!!)
                }
                categoryAdapter = CategoryAdapter(this@StockActivity, categoryArrayList)

                binding.RvCat.adapter = categoryAdapter
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
        val btn = findViewById<FloatingActionButton>(R.id.st_floatingActionButton)
        val addBtn = findViewById<FloatingActionButton>(R.id.st_floatingActionButton2)
        val addCatBtn = findViewById<FloatingActionButton>(R.id.st_floatingActionButton3)
        if (!clicked){
            addBtn.startAnimation(fromBottom)
            addCatBtn.startAnimation(fromBottom)
            btn.startAnimation(rotateOpen)
        }else{
            addBtn.startAnimation(toBottom)
            addCatBtn.startAnimation(toBottom)
            btn.startAnimation(rotateClose)
        }

    }

    private fun setVisibility(clicked : Boolean) {
        val addBtn = findViewById<FloatingActionButton>(R.id.st_floatingActionButton2)
        val addCatBtn = findViewById<FloatingActionButton>(R.id.st_floatingActionButton3)
        if(!clicked){
            addBtn.visibility = View.VISIBLE
            addCatBtn.visibility = View.VISIBLE
        }
        else{
            addBtn.visibility = View.INVISIBLE
            addCatBtn.visibility = View.INVISIBLE
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}