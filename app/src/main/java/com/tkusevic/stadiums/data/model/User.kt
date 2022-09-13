package com.tkusevic.stadiums.data.model

import java.io.Serializable

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    var role: String = "",
    var createdAt: String = "",
    var updatedAt: String = "",
) : Serializable