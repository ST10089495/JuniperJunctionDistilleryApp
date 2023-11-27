package com.example.juniperjunctiondistillery.menu.bookings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.juniperjunctiondistillery.databinding.ActivityBookingDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BookingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailsBinding
    private var bookId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBookingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookId = intent.getStringExtra("bookId")!!

        loadBookDetails()
    }

    private fun loadBookDetails() {
        val bookingRef = FirebaseDatabase.getInstance().getReference("bookings").child(bookId)
        bookingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = "${snapshot.child("Name").value}"
                val email = "${snapshot.child("Email").value}"
                val date = "${snapshot.child("Date").value}"
                val time = "${snapshot.child("PreferredTime").value}"
                val experi = "${snapshot.child("ExperienceType").value}"


                // Set the data in your UI components
                binding.NameTV.text = name
                binding.emailTV.text = email
                binding.dateTV.text = date
                binding.timeTV.text = time
                binding.ExperiTV.text = experi

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }
    }
