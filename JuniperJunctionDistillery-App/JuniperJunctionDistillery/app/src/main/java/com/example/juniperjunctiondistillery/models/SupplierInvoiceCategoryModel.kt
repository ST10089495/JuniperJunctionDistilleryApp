package com.example.juniperjunctiondistillery.models

class SupplierInvoiceCategoryModel {
    var SupplierCategoryInvoiceID : String=""
    var SupplierCategoryInvoice : String=""

    constructor()
    constructor(SupplierCategoryInvoiceID: String, SupplierCategoryInvoice: String) {
        this.SupplierCategoryInvoiceID = SupplierCategoryInvoiceID
        this.SupplierCategoryInvoice = SupplierCategoryInvoice
    }

}