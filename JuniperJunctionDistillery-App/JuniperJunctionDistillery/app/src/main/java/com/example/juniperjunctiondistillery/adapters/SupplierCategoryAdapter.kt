package com.example.juniperjunctiondistillery.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.juniperjunctiondistillery.databinding.RowSuppcategoryBinding
import com.example.juniperjunctiondistillery.menu.suppliers.FilterSupplierCategories
import com.example.juniperjunctiondistillery.menu.suppliers.SuppliersListActivity
import com.example.juniperjunctiondistillery.models.SupplierCategoryModel
import com.google.firebase.database.FirebaseDatabase

class SupplierCategoryAdapter : RecyclerView.Adapter<SupplierCategoryAdapter.HolderSupplierCategory> ,Filterable{

    private val context : Context
    var suppCategoryArrayList : ArrayList<SupplierCategoryModel>

    private var filterList:ArrayList<SupplierCategoryModel>
    private var filter: FilterSupplierCategories?=null

    private lateinit var binding: RowSuppcategoryBinding

    constructor(context: Context, suppCategoryArrayList: ArrayList<SupplierCategoryModel>) {
        this.context = context
        this.suppCategoryArrayList = suppCategoryArrayList
        this.filterList = suppCategoryArrayList
    }


    inner class HolderSupplierCategory(itemView: View): RecyclerView.ViewHolder(itemView){
        var suppCategoryTV:TextView=binding.TvSuppCat
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderSupplierCategory {
        binding = RowSuppcategoryBinding.inflate(LayoutInflater.from(context),parent,false)
        return HolderSupplierCategory(binding.root)
    }

    override fun getItemCount(): Int {
        return suppCategoryArrayList.size
    }

    override fun onBindViewHolder(holder: HolderSupplierCategory, position: Int) {
        val model = suppCategoryArrayList[position]
        val suppId = model.SupplierCategoryID
        val suppCategory = model.SupplierCategory

        holder.suppCategoryTV.text = suppCategory


        holder.itemView.setOnClickListener {
            val intent = Intent(context, SuppliersListActivity::class.java)
            intent.putExtra("suppId",suppId)
            intent.putExtra("supp",suppCategory)
            context.startActivity(intent)

        }
    }



    override fun getFilter(): Filter {
        if(filter == null){
            filter = FilterSupplierCategories(filterList,this)
        }
        return filter as FilterSupplierCategories
    }
}