package com.example.juniperjunctiondistillery.menu.bookings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.adapters.BookingsAdapter
import com.example.juniperjunctiondistillery.adapters.EmployeeAdapter
import com.example.juniperjunctiondistillery.databinding.ActivityBookingsBinding
import com.example.juniperjunctiondistillery.databinding.ActivityEmployeeBinding
import com.example.juniperjunctiondistillery.models.BookingModel
import com.example.juniperjunctiondistillery.models.EmployeeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingsBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var bookingArrayList: ArrayList<BookingModel>
    private lateinit var bookingAdapter: BookingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()
        loadEmployees()
    }

    private fun loadEmployees() {
        bookingArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("bookings")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookingArrayList.clear()
                for(ds in snapshot.children){
                    val model = ds.getValue(BookingModel::class.java)
                    bookingArrayList.add(model!!)
                }
                bookingAdapter = BookingsAdapter(this@BookingsActivity,bookingArrayList)
                binding.RvBooking.adapter=bookingAdapter
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