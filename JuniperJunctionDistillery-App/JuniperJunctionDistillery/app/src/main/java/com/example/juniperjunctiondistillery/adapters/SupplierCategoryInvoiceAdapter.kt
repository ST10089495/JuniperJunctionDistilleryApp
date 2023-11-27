package com.example.juniperjunctiondistillery.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.juniperjunctiondistillery.databinding.RowSuppcategoryBinding
import com.example.juniperjunctiondistillery.menu.suppliers.SupplierInvoiceListActivity
import com.example.juniperjunctiondistillery.models.SupplierInvoiceCategoryModel

class SupplierCategoryInvoiceAdapter :
    RecyclerView.Adapter<SupplierCategoryInvoiceAdapter.HolderSupplierInvCat> {

    private val context: Context
    private val catInvArrayList: ArrayList<SupplierInvoiceCategoryModel>
    private lateinit var binding: RowSuppcategoryBinding

    constructor(context: Context, catInvArrayList: ArrayList<SupplierInvoiceCategoryModel>) {
        this.context = context
        this.catInvArrayList = catInvArrayList
    }


    inner class HolderSupplierInvCat(itemView: View) : ViewHolder(itemView) {
        val catInvTV: TextView = binding.TvSuppCat
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderSupplierInvCat {
        binding = RowSuppcategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderSupplierInvCat(binding.root)
    }

    override fun getItemCount(): Int {
        return catInvArrayList.size
    }

    override fun onBindViewHolder(holder: HolderSupplierInvCat, position: Int) {
        val model = catInvArrayList[position]
        val suppCatInvId = model.SupplierCategoryInvoiceID
        val suppCatInv = model.SupplierCategoryInvoice

        holder.catInvTV.text = suppCatInv

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SupplierInvoiceListActivity::class.java)
            intent.putExtra("suppCatInvId",suppCatInvId)
            intent.putExtra("suppCatInv",suppCatInv)
            context.startActivity(intent)

        }
    }
}