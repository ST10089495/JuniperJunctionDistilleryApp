package com.example.juniperjunctiondistillery.menu.employee

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.adapters.EmployeeAdapter
import com.example.juniperjunctiondistillery.databinding.ActivityEmployeeBinding
import com.example.juniperjunctiondistillery.models.EmployeeModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EmployeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var employeeArrayList: ArrayList<EmployeeModel>
    private lateinit var employeeAdapter: EmployeeAdapter

    private val rotateOpen : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
    private val rotateClose : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
    private val fromBottom : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottom : Animation by lazy{ AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}
    private var clicked = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()
        loadEmployees()

        binding.empsearchEt.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try{
                    employeeAdapter.filter.filter(s)
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
            val intent = Intent(this, AddEmployeeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadEmployees() {
        employeeArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Employees")
        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                employeeArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(EmployeeModel::class.java)
                    employeeArrayList.add(model!!)
                }
                employeeAdapter = EmployeeAdapter(this@EmployeeActivity,employeeArrayList)
                binding.RvEmp.adapter=employeeAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }


    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked : Boolean) {
        val btn = findViewById<FloatingActionButton>(R.id.st_floatingActionButton)
        val addBtn = findViewById<FloatingActionButton>(R.id.st_floatingActionButton2)
        if (!clicked){
            addBtn.startAnimation(fromBottom)
            btn.startAnimation(rotateOpen)
        }else{
            addBtn.startAnimation(toBottom)
            btn.startAnimation(rotateClose)
        }

    }

    private fun setVisibility(clicked : Boolean) {
        val addBtn = findViewById<FloatingActionButton>(R.id.st_floatingActionButton2)
        if(!clicked){
            addBtn.visibility = View.VISIBLE
        }
        else{
            addBtn.visibility = View.INVISIBLE
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}