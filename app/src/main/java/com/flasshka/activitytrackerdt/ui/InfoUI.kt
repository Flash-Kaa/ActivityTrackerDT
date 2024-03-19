package com.flasshka.activitytrackerdt.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Info() {
    Text(
        text = "this is my app",
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        fontSize = 24.sp
    )
}