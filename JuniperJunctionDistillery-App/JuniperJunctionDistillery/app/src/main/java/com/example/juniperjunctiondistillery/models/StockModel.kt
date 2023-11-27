package com.example.juniperjunctiondistillery.models

class StockModel {
    var ID:String = ""
    var Name:String =""
    var Description:String = ""
    var Price:String = ""
    var Quantity:String = ""
    var Arrival:String = ""
    var CategoryID:String = ""

    constructor()
    constructor(
        ID: String,
        Name: String,
        Description: String,
        Price: String,
        Quantity: String,
        Arrival: String,
        CategoryID: String
    ) {
        this.ID = ID
        this.Name = Name
        this.Description = Description
        this.Price = Price
        this.Quantity = Quantity
        this.Arrival = Arrival
        this.CategoryID = CategoryID
    }


}