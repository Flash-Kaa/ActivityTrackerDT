package com.flasshka.presentation.creating

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.flasshka.presentation.R

@Composable
fun SaveButton(
    getSaveButtonIsEnabled: () -> Boolean,
    getAction: (CreateHabitActionType) -> (() -> Unit)
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Button(
            modifier = Modifier
                .padding(10.dp)
                .size(80.dp),
            shape = RoundedCornerShape(40.dp),
            onClick = getAction(CreateHabitActionType.NavigateToListOfHabits),
            enabled = getSaveButtonIsEnabled()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.round_done_36),
                contentDescription = "add_button",
                tint = Color.White
            )
        }
    }
}