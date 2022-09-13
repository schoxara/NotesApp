package com.tkusevic.stadiums.ui.stadiums.views

import com.tkusevic.stadiums.data.model.Notes

interface AllNnotesView {

    fun setNotes(notes: List<Notes>)

    fun addNotes(nontes: List<Notes>)
}