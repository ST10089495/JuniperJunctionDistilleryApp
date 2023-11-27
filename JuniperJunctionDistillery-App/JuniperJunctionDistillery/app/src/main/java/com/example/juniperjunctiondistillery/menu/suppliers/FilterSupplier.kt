package com.example.juniperjunctiondistillery.menu.suppliers

import android.widget.Filter
import com.example.juniperjunctiondistillery.adapters.SupplierAdapter
import com.example.juniperjunctiondistillery.models.SupplierModel

class FilterSupplier :Filter {

    var filterList :ArrayList<SupplierModel>
    var supplierAdapter : SupplierAdapter

    constructor(filterList: ArrayList<SupplierModel>, supplierAdapter: SupplierAdapter) {
        this.filterList = filterList
        this.supplierAdapter = supplierAdapter
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint:CharSequence? = constraint
        val results = FilterResults()
        if(constraint !=null && constraint.isNotEmpty()){
            constraint=constraint.toString().lowercase()
            var filteredModels = ArrayList<SupplierModel>()
            for(i in filterList.indices){
                if(filterList[i].SupplierName.lowercase().contains(constraint)){
                    filteredModels.add(filterList[i])
                }
            }
            results.count =filteredModels.size
            results.values =filteredModels
        }
        else{
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        supplierAdapter.suppArrayList = results.values as ArrayList<SupplierModel>

        supplierAdapter.notifyDataSetChanged()

    }
}