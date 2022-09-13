package com.tkusevic.stadiums.ui.stadiums.holder

import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.Menu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tkusevic.stadiums.commons.extensions.onClick
import com.tkusevic.stadiums.data.model.Notes
import com.tkusevic.stadiums.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.holder_note.view.*


class NotesViewHolder(private val listener: OnItemClickListener, itemView: View) :
    RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {


    fun setItem(note: Notes, position: Int) = with(itemView) {
        number.text = "#${position + 1}"
        title.text = note.title
        description.text = String.format("%s", note.description)
        note.user?.run {
            author.text = String.format("Author: %s", this.name)

        }
        createdAt.text = String.format("CreatedAt: %s", note.createdAt)
        onClick { listener.onNotesClick(note) }

        this.setOnCreateContextMenuListener(this@NotesViewHolder);

    }

    override fun onCreateContextMenu(
        menu: ContextMenu, v: View?,
        menuInfo: ContextMenuInfo?
    ) {
        //menuInfo is null
        menu.add(
            Menu.NONE, 1231,
            Menu.NONE, "Edit"
        )
        menu.add(
            Menu.NONE, 2231,
            Menu.NONE, "Delete"
        )
    }
}