package com.example.juniperjunctiondistillery.menu.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.account.SignInActivity
import com.example.juniperjunctiondistillery.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var user: FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Profile"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        user = FirebaseAuth.getInstance()
        loadUserInfo()
        binding.signoutbtn.setOnClickListener {
            user.signOut()
            startActivity(
                Intent(this, SignInActivity::class.java)
            )
        }

    }

    private fun loadUserInfo() {
        val textView = findViewById<TextView>(R.id.TVemail)
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val email = currentUser.email
            textView.text = email

        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
