package com.tkusevic.stadiums.commons.extensions

import android.content.Context
import android.widget.Toast

fun Context?.toast(message: String) {
    this?.let { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
}

