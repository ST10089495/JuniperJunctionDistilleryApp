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
import com.example.juniperjunctiondistillery.databinding.RowSuppBinding
import com.example.juniperjunctiondistillery.menu.suppliers.EditSupplierActivity
import com.example.juniperjunctiondistillery.menu.suppliers.FilterSupplier
import com.example.juniperjunctiondistillery.menu.suppliers.SupplierDetailsActivity
import com.example.juniperjunctiondistillery.models.SupplierModel
import com.google.firebase.database.FirebaseDatabase

class SupplierAdapter:RecyclerView.Adapter<SupplierAdapter.HolderSupplier>, Filterable{

    private var context:Context
    var suppArrayList:ArrayList<SupplierModel>
    private val filterList:ArrayList<SupplierModel>

    private lateinit var binding : RowSuppBinding

    var filter : FilterSupplier? = null

    constructor(context: Context, suppArrayList: ArrayList<SupplierModel>) : super() {
        this.context = context
        this.suppArrayList = suppArrayList
        this.filterList = suppArrayList
    }

    inner class HolderSupplier(itemView: View):RecyclerView.ViewHolder(itemView){
        val suppName = binding.tvSuppName
        val suppDescr = binding.tvSuppDescr
        val suppMoreBtn = binding.SuppMoreBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderSupplier {
        binding = RowSuppBinding.inflate(LayoutInflater.from(context),parent,false)
        return HolderSupplier(binding.root)
    }
    override fun onBindViewHolder(holder: HolderSupplier, position: Int) {
        val model = suppArrayList[position]
        val suppID = model.ID
        val suppName = model.SupplierName
        val suppDescr = model.SupplierDescription
        val suppConNum = model.SupplierContactNumber
        val suppEmail = model.SupplierEmail
        val suppStock = model.SupplierStock
        val suppCat = model.SupplierCategory

        holder.suppName.text=suppName
        holder.suppDescr.text=suppDescr

        holder.suppMoreBtn.setOnClickListener {
            moreOptionsDialog(model, holder)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, SupplierDetailsActivity::class.java)
            intent.putExtra("suppId",suppID)
            context.startActivity(intent)
        }


    }

    private fun moreOptionsDialog(model: SupplierModel, holder: SupplierAdapter.HolderSupplier) {
        val suppId = model.ID
        val suppName = model.SupplierName

        val options = arrayOf("Edit", "Delete")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Option:")
            .setItems(options) { dialog, position ->
                if (position == 0) {
                    val intent = Intent(context, EditSupplierActivity::class.java)
                    intent.putExtra("suppId", suppId)
                    context.startActivity(intent)
                } else if (position == 1) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete")
                        .setMessage("Are You Sure You Want To Delete This?")
                        .setPositiveButton("Confirm") { a, d ->
                            Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show()
                            deleteStock(model, suppId, suppName)

                        }
                        .setNegativeButton("Cancel") { a, d ->
                            a.dismiss()
                        }
                        .show()
                }
            }.show()
    }

    private fun deleteStock(model: SupplierModel, suppId: String, suppName: String) {
        val id = model.ID
        val ref = FirebaseDatabase.getInstance().getReference("Suppliers")
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

    override fun getItemCount(): Int {
        return suppArrayList.size
    }

    override fun getFilter(): Filter {
        if(filter==null){
            filter = FilterSupplier(filterList,this)
        }
        return  filter as FilterSupplier
    }


}