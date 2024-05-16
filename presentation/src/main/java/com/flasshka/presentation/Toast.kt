package com.flasshka.presentation

import android.content.Context
import android.widget.Toast
import javax.inject.Inject

class Toast @Inject constructor() {
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    fun showToast(content: String) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }
}