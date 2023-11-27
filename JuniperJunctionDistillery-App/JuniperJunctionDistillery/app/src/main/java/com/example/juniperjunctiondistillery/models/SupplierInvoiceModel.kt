package com.example.juniperjunctiondistillery.models

class SupplierInvoiceModel {
    var ID: String = ""
    var Supplier: String = ""
    var Category: String =""
    var DeliveryNotes: String = ""
    var TotalPrice: String = ""
    var DispatchDate: String = ""
    var DeliveryDate: String = ""

    constructor()
    constructor(
        ID: String,
        Supplier: String,
        DeliveryNotes: String,
        TotalPrice: String,
        DispatchDate: String,
        DeliveryDate: String,
        Category: String
    ) {
        this.ID = ID
        this.Supplier = Supplier
        this.DeliveryNotes = DeliveryNotes
        this.TotalPrice = TotalPrice
        this.DispatchDate = DispatchDate
        this.DeliveryDate = DeliveryDate
        this.Category = Category
    }


}