package com.flasshka.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Info() {
    Text(
        text = stringResource(R.string.info_content),
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        fontSize = 24.sp
    )
}