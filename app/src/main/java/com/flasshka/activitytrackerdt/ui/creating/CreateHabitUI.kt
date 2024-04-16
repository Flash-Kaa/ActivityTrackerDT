package com.flasshka.activitytrackerdt.ui.creating

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.flasshka.activitytrackerdt.R
import com.flasshka.activitytrackerdt.models.habit.HabitPriority
import com.flasshka.activitytrackerdt.models.habit.HabitType

@Composable
fun DrawCreateHabitUI(
    vm: CreateHabitVM,
    navController: NavController,
    habitIdToChange: Long? = null
) {
    CreateHabitUI(
        getHabitState = vm.getHabitStateAction(habitIdToChange),
        getNameIsCorrect = vm.getNameIsCorrectAction(),
        getPeriodicityCountIsCorrect = vm.getPeriodicityCountIsCorrectAction(),
        getPeriodicityDaysCountIsCorrect = vm.getPeriodicityDaysCountIsCorrectAction(),
        getAllowedColor = vm.getAllowedColorAction(),
        getSaveButtonIsEnabled = vm.getSaveButtonIsEnabled(),
        onNameChange = vm.getOnNameChangeAction(),
        onDescriptionChange = vm.getOnDescriptionChangeAction(),
        onPriorityChange = vm.getOnPriorityChangeAction(),
        onPeriodicityCountChange = vm.getOnPeriodicityCountIsCorrectAction(),
        onPeriodicityDaysCountChange = vm.getOnPeriodicityDaysCountIsCorrectAction(),
        onTypeChange = vm.getOnTypeChangeAction(),
        onColorChange = vm.getOnColorChangeAction(),
        onSaveHabit = vm.getOnSaveHabitAction(navController)
    )
}

@Composable
private fun CreateHabitUI(
    getHabitState: () -> CreateHabitState,
    getNameIsCorrect: () -> Boolean,
    getPeriodicityCountIsCorrect: () -> Boolean,
    getPeriodicityDaysCountIsCorrect: () -> Boolean,
    getAllowedColor: () -> List<Color>,
    getSaveButtonIsEnabled: () -> Boolean,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (HabitPriority) -> Unit,
    onPeriodicityCountChange: (String) -> Unit,
    onPeriodicityDaysCountChange: (String) -> Unit,
    onTypeChange: (HabitType) -> Unit,
    onColorChange: (Color) -> Unit,
    onSaveHabit: () -> Unit
) {
    /*if (id != habitIdToChange) {

        id = habitIdToChange ?: (vm.habits.maxOf { it.id } + 1)
        name = ""
        description = ""
        priority = HabitPriority.URGENT_AND_IMPORTANT
        type = HabitType.BAD
        periodicityCount = 0
        periodicityDays = 0
        color = Color.Black
        habitIdToChange?.let { longId ->

            id = longId

            vm.habits.first { it.id == longId }
                .also {
                    name = it.name
                    description = it.description
                    priority = it.priority
                    type = it.type
                    periodicityCount = it.periodicity.count
                    periodicityDays = it.periodicity.days
                    color = it.color
                }
        }
    }*/

    Column(
        modifier = Modifier.padding(10.dp),
    ) {
        Text(
            text = stringResource(R.string.CreateOrEditHabit),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            fontSize = 28.sp
        )

        TextField(
            value = getHabitState().name,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.HabitName)) },
            colors = TextFieldDefaults.colors(errorContainerColor = Color.Red),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            isError = !getNameIsCorrect()
        )

        TextField(
            value = getHabitState().description,
            label = {
                Text(stringResource(R.string.HabitDescription))
            },
            onValueChange = onDescriptionChange,
            singleLine = false,
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        )

        Text(
            text = stringResource(R.string.Periodicity),
            modifier = Modifier.padding(top = 15.dp)
        )

        Row {
            PeriodicityField(
                text = stringResource(R.string.PeriodicityInDays),
                getValue = { getHabitState().periodicityDaysCount },
                onPeriodicityChange = onPeriodicityDaysCountChange,
                isCorrect = getPeriodicityDaysCountIsCorrect
            )

            PeriodicityField(
                text = stringResource(R.string.PeriodicityInCount),
                getValue = { getHabitState().periodicityCount },
                onPeriodicityChange = onPeriodicityCountChange,
                isCorrect = getPeriodicityCountIsCorrect
            )
        }

        PriorityField(
            getHabitState = getHabitState,
            onPriorityChange = onPriorityChange
        )

        Text(
            text = stringResource(R.string.HabitColor),
            modifier = Modifier.padding(top = 20.dp)
        )

        ColorChooser(
            getHabitState = getHabitState,
            getAllowedColor = getAllowedColor,
            onColorChange = onColorChange
        )

        Text(
            text = stringResource(R.string.HabitType),
            modifier = Modifier.padding(top = 20.dp)
        )

        TypeButtons(
            getHabitState = getHabitState,
            onTypeChange = onTypeChange
        )
    }

    SaveButton(
        getSaveButtonIsEnabled = getSaveButtonIsEnabled,
        onSaveHabit = onSaveHabit
    )
}

@Composable
private fun SaveButton(
    getSaveButtonIsEnabled: () -> Boolean,
    onSaveHabit: () -> Unit
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
            onClick = onSaveHabit,
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

@Composable
private fun PeriodicityField(
    text: String,
    getValue: () -> String,
    onPeriodicityChange: (String) -> Unit,
    isCorrect: () -> Boolean
) {
    TextField(
        value = getValue(),
        label = { Text(text) },
        onValueChange = onPeriodicityChange,
        modifier = Modifier
            .padding(10.dp)
            .width(150.dp),
        isError = !isCorrect(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        colors = TextFieldDefaults.colors(errorContainerColor = Color.Red)
    )
}

@Composable
private fun PriorityField(
    getHabitState: () -> CreateHabitState,
    onPriorityChange: (HabitPriority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextField(
            value = getHabitState().priority.name,
            onValueChange = {},
            modifier = Modifier
                .padding(top = 15.dp)
                .clickable { expanded = !expanded },
            label = { Text(stringResource(R.string.HabitPriority)) },
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        if (!expanded)
                            R.drawable.baseline_arrow_drop_down_24
                        else
                            R.drawable.baseline_arrow_drop_up_24
                    ),
                    contentDescription = "arror"
                )
            },
            colors = TextFieldDefaults.colors(
                disabledTextColor = colorResource(id = R.color.TextField_TextColor)
            ),
            enabled = false
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            HabitPriority.entries.forEach {
                DropdownMenuItem(
                    text = {
                        Text(text = it.toString())
                    },
                    onClick = {
                        onPriorityChange(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun TypeButtons(
    getHabitState: () -> CreateHabitState,
    onTypeChange: (HabitType) -> Unit
) {
    HabitType.entries.forEach {
        Row(
            modifier = Modifier
                .clickable { onTypeChange(it) }
                .size(width = 200.dp, height = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = getHabitState().type == it,
                onClick = { onTypeChange(it) }
            )

            Text(
                text = it.toString(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ColorChooser(
    getHabitState: () -> CreateHabitState,
    getAllowedColor: () -> List<Color>,
    onColorChange: (Color) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        items(getAllowedColor()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(it)
                    .clickable { onColorChange(it) }
                    .size(40.dp)
            ) {
                if (it == getHabitState().color) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.round_done_36),
                        tint = Color(75, 0, 130),
                        contentDescription = "selected"
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}
