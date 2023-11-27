package com.example.juniperjunctiondistillery.menu.bookings

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.juniperjunctiondistillery.databinding.ActivityEditBookingsBinding
import com.example.juniperjunctiondistillery.menu.employee.EditEmployeeActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditBookingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBookingsBinding
    private companion object {
        private const val TAG = "BOOKING_EDIT_TAG"
    }
    private var bookId = ""
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBookingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        bookId = intent.getStringExtra("bookId")!!

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        loadEmpInfo()
        binding.empUpdateBtn.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {

    }

    private fun loadEmpInfo() {
        Log.d(TAG, "loadEmpInfo: Loading Booking Info")

        val ref = FirebaseDatabase.getInstance().getReference("bookings")
        ref.child(bookId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("Name").value.toString()
                    val email = snapshot.child("Email").value.toString()
                    val date = snapshot.child("Date").value.toString()
                    val time = snapshot.child("PreferredTime").value.toString()
                    val experi = snapshot.child("ExperienceType").value.toString()

                    binding.nameEt.setText(name)
                    binding.emailEt.setText(email)
                    binding.dateEt.setText(date)
                    binding.timeEt.setText(time)
                    binding.experiEt.setText(experi)


                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}