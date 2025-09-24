package com.raj.tngpractice.feature.user.model

data class User (
    var id : Int = 0,
    var name : String = "",
    var username : String = "",
    var email : String = "",
    var address : AddressItem = AddressItem(),
    var phone : String = "",
    var website : String = "",
    var company : CompanyItem = CompanyItem()
)