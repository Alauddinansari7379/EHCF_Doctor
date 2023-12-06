package com.example.ehcf_doctor.HealthCube.Model

data class DataModal(
    // on below line we are creating
    // variables for name and job
    var firstName: String,
    var dob: String,
    var gender: String,
    var phoneNumber: String,
    var operatorPhoneNumber: String,
    var externalPatientId: String,
    var testList: ArrayList<String>,
//      var countryName: String,
//      var email: String,
//      var state: String,
//     var postelCode: String,

)