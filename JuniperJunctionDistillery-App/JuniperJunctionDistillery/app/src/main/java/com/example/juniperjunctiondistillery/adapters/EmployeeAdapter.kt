package com.example.juniperjunctiondistillery.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.juniperjunctiondistillery.databinding.RowEmpBinding
import com.example.juniperjunctiondistillery.menu.employee.EditEmployeeActivity
import com.example.juniperjunctiondistillery.menu.employee.EmployeeDetailsActivity
import com.example.juniperjunctiondistillery.menu.employee.FilterEmployee
import com.example.juniperjunctiondistillery.models.EmployeeModel
import com.google.firebase.database.FirebaseDatabase

class EmployeeAdapter: RecyclerView.Adapter<EmployeeAdapter.HolderEmployee>,Filterable{

    private val context:Context
    var employeeArrayList: ArrayList<EmployeeModel>
    private lateinit var binding: RowEmpBinding

    private var filterList : ArrayList<EmployeeModel>
    private var filter:FilterEmployee?=null

    constructor(context: Context, employeeArrayList: ArrayList<EmployeeModel>) {
        this.context = context
        this.employeeArrayList = employeeArrayList
        this.filterList=employeeArrayList
    }


    inner class HolderEmployee(itemView:View):RecyclerView.ViewHolder(itemView){
        var fnameTv:TextView=binding.tvFirstName
        var snameTv:TextView=binding.tvSurname
        var empMoreBtn : ImageButton=binding.empmoreBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderEmployee {
        binding = RowEmpBinding.inflate(LayoutInflater.from(context),parent,false)
        return HolderEmployee(binding.root)
    }

    override fun getItemCount(): Int {
        return employeeArrayList.size
    }

    override fun onBindViewHolder(holder: HolderEmployee, position: Int) {
        val model = employeeArrayList[position]
        val empId = model.ID
        val fname = model.FirstName
        val sname = model.Surname

        holder.fnameTv.text=fname
        holder.snameTv.text=sname

        holder.empMoreBtn.setOnClickListener {
            moreOptionsDialog(model,holder )
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EmployeeDetailsActivity::class.java)
            intent.putExtra("empId",empId)
            context.startActivity(intent)
        }
    }

    private fun moreOptionsDialog(model: EmployeeModel, holder: EmployeeAdapter.HolderEmployee) {
        val empId = model.ID
        val empName = model.FirstName
        val options = arrayOf("Edit", "Delete")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Option:")
            .setItems(options) { dialog, position ->
                if (position == 0) {
                    val intent = Intent(context, EditEmployeeActivity::class.java)
                    intent.putExtra("empId", empId)
                    context.startActivity(intent)
                } else if (position == 1) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete")
                        .setMessage("Are You Sure You Want To Delete This Category?")
                        .setPositiveButton("Confirm") { a, d ->
                            Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show()
                            deleteStock(model, empName, empName)

                        }
                        .setNegativeButton("Cancel") { a, d ->
                            a.dismiss()
                        }
                        .show()
                }
            }.show()
    }

    private fun deleteStock(model: EmployeeModel, empName: String, empName1: String) {
        val id = model.ID
        val ref = FirebaseDatabase.getInstance().getReference("Employees")
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
        if(filter == null){
            filter = FilterEmployee(filterList, this)

        }
        return filter as FilterEmployee

    }
}