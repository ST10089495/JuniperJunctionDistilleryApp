package com.example.juniperjunctiondistillery.menu.stock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.juniperjunctiondistillery.R
import com.example.juniperjunctiondistillery.adapters.StockAdapter
import com.example.juniperjunctiondistillery.databinding.ActivityStockListBinding
import com.example.juniperjunctiondistillery.models.StockModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StockListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockListBinding

    private companion object {
        const val TAG = "STOCK_LIST_TAG"
    }

    private var categoryId = ""
    private var category = ""

    private lateinit var stockArrayList: ArrayList<StockModel>
    private lateinit var stockAdapter: StockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionbar = supportActionBar
        actionbar!!.title = "Juniper Junction Distillery"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        categoryId = intent.getStringExtra("CategoryId")!!
        category = intent.getStringExtra("Category")!!

        loadStockList()
        binding.searchEt1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    stockAdapter.filter!!.filter(s)
                } catch (e: java.lang.Exception) {
                    Log.d(TAG, "onTextChanged: ${e.message}")
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


    }

    private fun loadStockList() {
        stockArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Stock")
        ref.orderByChild("Category").equalTo(categoryId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    stockArrayList.clear()
                    for (ds in snapshot.children) {
                        val model = ds.getValue(StockModel::class.java)
                        if (model != null) {
                            stockArrayList.add(model)
                            Log.d(TAG, "onDataChange: ${model.Name} ${model.CategoryID}")
                        }
                    }
                    stockAdapter = StockAdapter(this@StockListActivity, stockArrayList)
                    binding.stockRv.adapter = stockAdapter
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