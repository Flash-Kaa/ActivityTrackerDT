package com.flasshka.presentation.creating

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.flasshka.presentation.R

@Composable
fun ColorChooser(
    getHabitState: () -> CreateHabitState,
    colors: List<Color>,
    getAction: (CreateHabitActionType) -> (() -> Unit)
) {
    Text(
        text = stringResource(R.string.HabitColor),
        modifier = Modifier.padding(top = 20.dp)
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        items(colors) {
            ColorDrawer(
                color = it,
                getHabitState = getHabitState,
                getAction = getAction
            )
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}

@Composable
private fun ColorDrawer(
    color: Color,
    getHabitState: () -> CreateHabitState,
    getAction: (CreateHabitActionType) -> (() -> Unit)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(color)
            .clickable(onClick = getAction(CreateHabitActionType.OnColorChange(color)))
            .size(40.dp)
    ) {
        if (color == getHabitState().color) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.round_done_36),
                tint = Color(75, 0, 130),
                contentDescription = "selected"
            )
        }
    }
}