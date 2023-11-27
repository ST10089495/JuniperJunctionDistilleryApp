package com.example.juniperjunctiondistillery.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.juniperjunctiondistillery.databinding.RowCategoryBinding
import com.example.juniperjunctiondistillery.menu.stock.FilterCategory
import com.example.juniperjunctiondistillery.menu.stock.StockListActivity
import com.example.juniperjunctiondistillery.models.CategoryModel
import com.google.firebase.database.FirebaseDatabase

 class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.HolderCategory> , Filterable{

    private val context: Context
    var categoryArrayList: ArrayList<CategoryModel>
    private  var filterList : ArrayList<CategoryModel>

    private var filter : FilterCategory? = null

    private lateinit var binding: RowCategoryBinding

    constructor(context: Context, categoryArrayList: ArrayList<CategoryModel>){
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent,false)
        return HolderCategory(binding.root)
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {
        val model = categoryArrayList[position]
        val id = model.id
        val category = model.category

        holder.categoryTv.text = category

        holder.deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("Are You Sure You Want To Delete This Category?")
                .setPositiveButton("Confirm"){a, d->
                    Toast.makeText(context, "Deleting...",Toast.LENGTH_SHORT).show()
                deleteCategory(model, holder)

                }
                .setNegativeButton("Cancel"){a, d->
                    a.dismiss()
                }
                .show()
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, StockListActivity::class.java)
            intent.putExtra("CategoryId", id)
            intent.putExtra("Category", category)
            context.startActivity(intent)

        }
    }

    private fun deleteCategory(model: CategoryModel, holder: HolderCategory) {
        val id = model.id
        val ref = FirebaseDatabase.getInstance().getReference("Stock Categories")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted!!!",Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener{ e->
                Toast.makeText(context, "Unable To Delete Due To ${e.message}",Toast.LENGTH_SHORT).show()

            }

    }

    inner class HolderCategory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryTv: TextView = binding.TvCat
        var deleteBtn: ImageButton = binding.deleteBtn
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = FilterCategory(filterList, this)

        }
        return filter as FilterCategory

    }
}

