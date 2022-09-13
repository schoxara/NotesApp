package com.tkusevic.stadiums.ui.notes_details

import com.tkusevic.stadiums.data.model.Notes

interface DetailsView {

    fun showData(note: Notes)
}