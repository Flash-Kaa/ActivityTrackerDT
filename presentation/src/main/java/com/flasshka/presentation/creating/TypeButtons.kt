package com.flasshka.presentation.creating

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.flasshka.domain.entities.Habit
import com.flasshka.presentation.R

@Composable
fun TypeButtons(
    getHabitState: () -> CreateHabitState,
    getAction: (CreateHabitActionType) -> (() -> Unit)
) {
    Text(
        text = stringResource(R.string.ChoseHabitType),
        modifier = Modifier.padding(top = 20.dp)
    )

    Habit.Type.entries.forEach {
        Row(
            modifier = Modifier
                .clickable(onClick = getAction(CreateHabitActionType.OnTypeChange(it)))
                .size(width = 200.dp, height = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = getHabitState().type == it,
                onClick = getAction(CreateHabitActionType.OnTypeChange(it))
            )

            Text(
                text = it.toString(),
                textAlign = TextAlign.Center,
            )
        }
    }
}