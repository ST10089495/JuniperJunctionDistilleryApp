package com.example.juniperjunctiondistillery.models

class EmployeeModel {

    var ID : String =""
    var FirstName : String =""
    var Surname : String =""
    var ContactNumber : String =""
    var Address : String =""
    var Manager : String =""

    constructor()
        constructor(
        ID: String,
        FirstName: String,
        Surname: String,
        ContactNumber: String,
        Address: String,
        Manager: String
    ) {
        this.ID = ID
        this.FirstName = FirstName
        this.Surname = Surname
        this.ContactNumber = ContactNumber
        this.Address = Address
        this.Manager = Manager
    }


}