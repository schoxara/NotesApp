package com.tkusevic.stadiums.data.model.request

data class LoginRequest(
    var username: String = "",
    var password: String = "",
)