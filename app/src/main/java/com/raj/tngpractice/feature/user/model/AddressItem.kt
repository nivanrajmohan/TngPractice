package com.raj.tngpractice.feature.user.model

data class AddressItem (
    var street : String = "",
    var suite : String = "",
    var city : String = "",
    var zipcode : String = "",
    var geo : LocationItem = LocationItem()
)
