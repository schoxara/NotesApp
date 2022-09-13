package com.tkusevic.stadiums.data.model

import java.io.Serializable

open class Notes(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    var createdAt: String = "",
    var updatedAt: String = "",
    var user: User?
) : Serializable