package com.tkusevic.stadiums.ui.stadiums.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tkusevic.stadiums.R
import com.tkusevic.stadiums.data.model.Notes
import com.tkusevic.stadiums.ui.listeners.OnItemClickListener
import com.tkusevic.stadiums.ui.stadiums.holder.NotesViewHolder

class AllNotesAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<NotesViewHolder>() {

    private val stadiums: MutableList<Notes> = mutableListOf()

    fun setItems(list: List<Notes>) {
        stadiums.clear()
        stadiums.addAll(list)
        notifyDataSetChanged()
    }

    fun addItems(stadiums: List<Notes>) {
        this.stadiums.addAll(stadiums)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_note, parent, false)
        return NotesViewHolder(listener, view)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val stadium = stadiums[position]
        holder.run {
            setItem(stadium, position)
            listener.let { this }
        }
    }

    override fun getItemCount(): Int = stadiums.size
    }