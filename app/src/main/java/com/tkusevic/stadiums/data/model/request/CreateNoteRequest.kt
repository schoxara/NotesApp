package com.tkusevic.stadiums.data.model.request

data class CreateNoteRequest(
    var title: String = "",
    var description: String = "",
)