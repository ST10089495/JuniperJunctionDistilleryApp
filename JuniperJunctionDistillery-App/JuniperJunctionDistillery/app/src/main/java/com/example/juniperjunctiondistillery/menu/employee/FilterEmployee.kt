package com.example.juniperjunctiondistillery.menu.employee

import android.widget.Filter
import com.example.juniperjunctiondistillery.adapters.EmployeeAdapter
import com.example.juniperjunctiondistillery.models.EmployeeModel

class FilterEmployee : Filter {
    private var filterList:ArrayList<EmployeeModel>
    private var adapterEmployee: EmployeeAdapter

    constructor(filterList: ArrayList<EmployeeModel>, adapterEmployee: EmployeeAdapter) : super() {
        this.filterList = filterList
        this.adapterEmployee = adapterEmployee
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        if(constraint != null && constraint.isNotEmpty()) {

            constraint = constraint.toString().uppercase()
            val filteredModels : ArrayList<EmployeeModel> = ArrayList()
            for(i in 0 until filterList.size){
                if(filterList[i].FirstName.uppercase().contains(constraint)){
                    filteredModels.add(filterList[i])
                }
                else if(filterList[i].Surname.uppercase().contains(constraint)){
                    filteredModels.add((filterList[i]))
                }

            }
            results.count = filteredModels.size
            results.values = filteredModels

        }
        else{
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterEmployee.employeeArrayList = results.values as ArrayList<EmployeeModel>
        adapterEmployee.notifyDataSetChanged()    }
}