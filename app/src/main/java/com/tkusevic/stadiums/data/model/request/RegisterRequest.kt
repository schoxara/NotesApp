package com.tkusevic.stadiums.data.model.request

data class RegisterRequest(
    var email: String = "",
    var name: String = "",
    var password: String = "",
    var confirm: String = ""
)