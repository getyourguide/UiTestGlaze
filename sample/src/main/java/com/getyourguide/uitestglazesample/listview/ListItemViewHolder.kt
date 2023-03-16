package com.getyourguide.uitestglazesample.listview

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.getyourguide.uitestglazesample.R

class ListItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(listItem: ListItem) {
        view.findViewById<TextView>(R.id.text).text = listItem.text
    }
}
