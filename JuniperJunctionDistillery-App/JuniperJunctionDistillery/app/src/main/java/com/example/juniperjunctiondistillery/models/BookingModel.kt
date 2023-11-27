package com.example.juniperjunctiondistillery.models

class BookingModel {
    var Id : String =""
    var Name : String =""
    var Email : String =""
    var Date : String =""
    var PreferredTime : String =""
    var ExperienceType : String =""

    constructor()
    constructor(
        Id: String,
        Name: String,
        Email: String,
        Date: String,
        PreferredTime: String,
        ExperienceType: String
    ) {
        this.Id = Id
        this.Name = Name
        this.Email = Email
        this.Date = Date
        this.PreferredTime = PreferredTime
        this.ExperienceType = ExperienceType
    }


}