package com.example.juniperjunctiondistillery.menu.stock

import android.widget.Filter
import com.example.juniperjunctiondistillery.adapters.StockAdapter
import com.example.juniperjunctiondistillery.models.StockModel

class FilterStock : Filter {
    var filterList:ArrayList<StockModel>
    var stockAdapter: StockAdapter

    constructor(filterList: ArrayList<StockModel>, stockAdapter: StockAdapter) : super() {
        this.filterList = filterList
        this.stockAdapter = stockAdapter
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint:CharSequence? = constraint
        val results = FilterResults()

        if (constraint != null && constraint.isNotEmpty()){

            constraint = constraint.toString().lowercase()
            var filteredModels = ArrayList<StockModel>()
            for(i in filterList.indices){
                if(filterList[i].Name.lowercase().contains(constraint)){
                    filteredModels.add(filterList[i])
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

        stockAdapter.stockArrayList = results.values as ArrayList<StockModel>

        stockAdapter.notifyDataSetChanged()
    }
}