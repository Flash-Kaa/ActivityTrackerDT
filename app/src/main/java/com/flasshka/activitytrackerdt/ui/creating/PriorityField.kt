package com.flasshka.activitytrackerdt.ui.creating

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.flasshka.activitytrackerdt.R
import com.flasshka.activitytrackerdt.models.habit.HabitPriority

@Composable
fun PriorityField(
    getHabitState: () -> CreateHabitState,
    getAction: (CreateHabitActionType) -> (() -> Unit)
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextField(
            value = getHabitState().priority.toString(),
            onValueChange = {},
            enabled = false,
            modifier = Modifier
                .padding(top = 15.dp)
                .clickable { expanded = !expanded },
            colors = TextFieldDefaults.colors(
                disabledTextColor = colorResource(id = R.color.TextField_TextColor)
            ),
            label = {
                Text(stringResource(R.string.ChoseHabitType))
            },
            trailingIcon = {
                IconForDropdownMenu { expanded }
            }
        )

        DropdownPriorityMenu(
            getExpended = { expanded },
            hidingAction = { expanded = false },
            getAction = getAction
        )
    }
}

@Composable
private fun IconForDropdownMenu(
    getExpended: () -> Boolean
) {
    Icon(
        imageVector = ImageVector.vectorResource(
            if (!getExpended())
                R.drawable.baseline_arrow_drop_down_24
            else
                R.drawable.baseline_arrow_drop_up_24
        ),
        contentDescription = "arror"
    )
}

@Composable
private fun DropdownPriorityMenu(
    getExpended: () -> Boolean,
    hidingAction: () -> Unit,
    getAction: (CreateHabitActionType) -> (() -> Unit)
) {
    DropdownMenu(
        expanded = getExpended(),
        onDismissRequest = hidingAction
    ) {
        HabitPriority.entries.forEach {
            DropdownMenuItem(
                text = { Text(text = it.toString()) },
                onClick = {
                    getAction(CreateHabitActionType.OnPriorityChange(it)).invoke()
                    hidingAction()
                }
            )
        }
    }
}