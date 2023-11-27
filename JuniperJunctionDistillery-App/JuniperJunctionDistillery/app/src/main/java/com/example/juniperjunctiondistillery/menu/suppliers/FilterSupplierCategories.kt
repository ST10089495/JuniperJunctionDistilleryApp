package com.example.juniperjunctiondistillery.menu.suppliers

import android.widget.Filter
import com.example.juniperjunctiondistillery.adapters.SupplierCategoryAdapter
import com.example.juniperjunctiondistillery.models.SupplierCategoryModel

class FilterSupplierCategories :Filter {

    private var filterList : ArrayList<SupplierCategoryModel>

    private var adapterSupplierCategory : SupplierCategoryAdapter

    constructor(filterList: ArrayList<SupplierCategoryModel>, adapterSupplierCategory: SupplierCategoryAdapter) {
        this.filterList = filterList
        this.adapterSupplierCategory = adapterSupplierCategory
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        if(constraint != null && constraint.isNotEmpty()){

            constraint = constraint.toString().uppercase()
            val filteredModel:ArrayList<SupplierCategoryModel> = ArrayList()
            for(i in 0 until  filterList.size){

                if(filterList[i].SupplierCategory.uppercase().contains(constraint)){
                    filteredModel.add(filterList[i])
                }
            }
            results.count = filteredModel.size
            results.values = filteredModel
        }
        else{
            results.count = filterList.size
            results.values = filterList

        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterSupplierCategory.suppCategoryArrayList = results.values as ArrayList<SupplierCategoryModel>

        adapterSupplierCategory.notifyDataSetChanged()
    }

}