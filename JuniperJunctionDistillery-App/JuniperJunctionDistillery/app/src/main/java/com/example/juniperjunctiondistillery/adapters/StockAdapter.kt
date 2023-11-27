package com.example.juniperjunctiondistillery.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.juniperjunctiondistillery.databinding.RowStockBinding
import com.example.juniperjunctiondistillery.menu.stock.EditStockActivity
import com.example.juniperjunctiondistillery.menu.stock.FilterStock
import com.example.juniperjunctiondistillery.menu.stock.StockDetailsActivity
import com.example.juniperjunctiondistillery.models.StockModel
import com.google.firebase.database.FirebaseDatabase

class StockAdapter : RecyclerView.Adapter<StockAdapter.HolderStock>, Filterable {
    private var context: Context
    var stockArrayList: ArrayList<StockModel>
    private lateinit var binding: RowStockBinding

    private val filterList: ArrayList<StockModel>
    private var filter: FilterStock? = null

    constructor(context: Context, stockArrayList: ArrayList<StockModel>) : super() {
        this.context = context
        this.stockArrayList = stockArrayList
        this.filterList = stockArrayList
    }

    inner class HolderStock(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStockName = binding.tvStockName
        val tvStockDescr = binding.tvStockDescr
        val moreBtn = binding.moreBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderStock {
        binding = RowStockBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderStock(binding.root)
    }

    override fun getItemCount(): Int {
        return stockArrayList.size
    }

    override fun onBindViewHolder(holder: HolderStock, position: Int) {
        val model = stockArrayList[position]
        val stockId = model.ID
        val stockName = model.Name
        val stockDescr = model.Description

        holder.tvStockName.text = stockName
        holder.tvStockDescr.text = stockDescr

        holder.moreBtn.setOnClickListener {
            moreOptionsDialog(model, holder)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context,StockDetailsActivity::class.java)
            intent.putExtra("stockId",stockId)
            context.startActivity(intent)
        }
    }

    private fun moreOptionsDialog(model: StockModel, holder: StockAdapter.HolderStock) {
        val stockId = model.ID
        val stockName = model.Name

        val options = arrayOf("Edit", "Delete")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Option:")
            .setItems(options) { dialog, position ->
                if (position == 0) {
                    val intent = Intent(context, EditStockActivity::class.java)
                    intent.putExtra("stockId", stockId)
                    context.startActivity(intent)
                } else if (position == 1) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete")
                        .setMessage("Are You Sure You Want To Delete This Category?")
                        .setPositiveButton("Confirm") { a, d ->
                            Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show()
                            deleteStock(model, stockId, stockName)

                        }
                        .setNegativeButton("Cancel") { a, d ->
                            a.dismiss()
                        }
                        .show()
                }
            }.show()
    }

    private fun deleteStock(model: StockModel, stockId: String, stockName: String) {
        val id = model.ID
        val ref = FirebaseDatabase.getInstance().getReference("Stock")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted!!!", Toast.LENGTH_SHORT).show()


            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Unable To Delete Due To ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterStock(filterList, this)
        }
        return filter as FilterStock
    }
}


