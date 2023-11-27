package com.example.juniperjunctiondistillery.models

class SupplierModel {
    var ID: String = ""
    var SupplierName: String = ""
    var SupplierDescription: String = ""
    var SupplierContactNumber: String = ""
    var SupplierEmail: String = ""
    var SupplierStock: String = ""
    var SupplierCategory: String = ""

    constructor()
    constructor(
        ID: String,
        SupplierName: String,
        SupplierDescription: String,
        SupplierContactNumber: String,
        SupplierEmail: String,
        SupplierStock: String,
        SupplierCategory: String
    ) {
        this.ID = ID
        this.SupplierName = SupplierName
        this.SupplierDescription = SupplierDescription
        this.SupplierContactNumber = SupplierContactNumber
        this.SupplierEmail = SupplierEmail
        this.SupplierStock = SupplierStock
        this.SupplierCategory = SupplierCategory
    }


}