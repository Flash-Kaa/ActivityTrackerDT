package com.flasshka.activitytrackerdt.ui

import android.content.Context
import android.widget.Toast

class Toast(private val context: Context) {
    fun showToast(content: String) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }
}