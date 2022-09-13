package com.tkusevic.stadiums.data.model

import java.io.Serializable

data class LoginResponse(
    val userId: String = "",
    val access_token: String = "",
) : Serializable