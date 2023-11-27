package com.example.juniperjunctiondistillery.menu.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.menu.bookings.BookingsActivity
import com.example.juniperjunctiondistillery.menu.employee.EmployeeActivity
import com.example.juniperjunctiondistillery.menu.profile.ProfileActivity
import com.example.juniperjunctiondistillery.menu.stock.StockActivity
import com.example.juniperjunctiondistillery.menu.suppliers.SuppliersActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val cardStock = findViewById<CardView>(R.id.card_stock)
        cardStock.setOnClickListener {
            val intent = Intent(this, StockActivity::class.java)
            startActivity(intent)
        }
        val cardEmp = findViewById<CardView>(R.id.card_employee)
        cardEmp.setOnClickListener {
            val intent = Intent(this, EmployeeActivity::class.java)
            startActivity(intent)
        }
        val cardSupp = findViewById<CardView>(R.id.card_suppliers)
        cardSupp.setOnClickListener {
            val intent = Intent(this, SuppliersActivity::class.java)
            startActivity(intent)
        }
        val cardBookings = findViewById<CardView>(R.id.card_bookings)
        cardBookings.setOnClickListener {
            val intent = Intent(this, BookingsActivity::class.java)
            startActivity(intent)
        }
        val cardProfile = findViewById<CardView>(R.id.card_profile)
        cardProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}