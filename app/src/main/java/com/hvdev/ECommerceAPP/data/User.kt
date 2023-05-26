package com.hvdev.ECommerceAPP.data

data class User(
    val firstName : String,
    val lastName : String,
    val email : String,
    var imagePath : String = ""
){
    //whenever you want to deal with firebase services you need to have your
    //empty constructor becase firebase will use that
    constructor(): this("","","","")
}
