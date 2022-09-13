package com.tkusevic.stadiums.commons.extensions

import android.view.View

fun View.onClick(onClick: () -> Unit) = setOnClickListener { onClick() }

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}
