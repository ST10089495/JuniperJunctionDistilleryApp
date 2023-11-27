package com.example.juniperjunctiondistillery.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.juniperjunctiondistillery.databinding.RowSuppInvBinding
import com.example.juniperjunctiondistillery.menu.suppliers.EditSupplierActivity
import com.example.juniperjunctiondistillery.menu.suppliers.EditSupplierInvoiceActivity
import com.example.juniperjunctiondistillery.menu.suppliers.SupplierDetailsActivity
import com.example.juniperjunctiondistillery.menu.suppliers.SupplierInvoiceDetailsActivity
import com.example.juniperjunctiondistillery.models.SupplierInvoiceModel
import com.google.firebase.database.FirebaseDatabase

class SupplierInvoiceAdapater:RecyclerView.Adapter<SupplierInvoiceAdapater.HolderSupplierInvoice> {

    private var context: Context
    private var suppInvArrayList:ArrayList<SupplierInvoiceModel>
    private lateinit var binding: RowSuppInvBinding

    constructor(context: Context, suppInvArrayList: ArrayList<SupplierInvoiceModel>) : super() {
        this.context = context
        this.suppInvArrayList = suppInvArrayList
    }


    inner class HolderSupplierInvoice(itemView: View):RecyclerView.ViewHolder(itemView){
        val invName = binding.tvInvoiceName
        val moreBtn = binding.suppinvmoreBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderSupplierInvoice {
        binding = RowSuppInvBinding.inflate(LayoutInflater.from(context),parent,false)
        return HolderSupplierInvoice(binding.root)
    }


    override fun onBindViewHolder(holder: HolderSupplierInvoice, position: Int) {
        val model = suppInvArrayList[position]
        val invID = model.ID
        val invName = model.Supplier

        holder.invName.text=invName

        holder.moreBtn.setOnClickListener {
            moreOptionsDialog(model, holder)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, SupplierInvoiceDetailsActivity::class.java)
            intent.putExtra("invId",invID)
            context.startActivity(intent)
        }
    }

    private fun moreOptionsDialog(model: SupplierInvoiceModel, holder: SupplierInvoiceAdapater.HolderSupplierInvoice) {
        val suppInvId = model.ID
        val suppInvName = model.Supplier

        val options = arrayOf("Edit", "Delete")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Option:")
            .setItems(options) { dialog, position ->
                if (position == 0) {
                    val intent = Intent(context, EditSupplierInvoiceActivity::class.java)
                    intent.putExtra("suppInvId", suppInvId)
                    context.startActivity(intent)
                } else if (position == 1) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete")
                        .setMessage("Are You Sure You Want To Delete This?")
                        .setPositiveButton("Confirm") { a, d ->
                            Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show()
                            deleteStock(model, suppInvId, suppInvName)

                        }
                        .setNegativeButton("Cancel") { a, d ->
                            a.dismiss()
                        }
                        .show()
                }
            }.show()
    }

    private fun deleteStock(model: SupplierInvoiceModel, suppInvId: String, suppInvName: String) {
        val invid = model.ID
        val ref = FirebaseDatabase.getInstance().getReference("Supplier Invoice")
        ref.child(invid)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Deleted!!!", Toast.LENGTH_SHORT).show()
                notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Unable To Delete Due To ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
    override fun getItemCount(): Int {
        return suppInvArrayList.size
    }

}