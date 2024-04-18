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
    Column(
        modifier = Modifier.padding(10.dp),
    ) {
        Title()

        NameField(
            getHabitState = getHabitState,
            getNameIsCorrect = getNameIsCorrect,
            onNameChange = onNameChange
        )

        DescriptionField(
            getHabitState = getHabitState,
            onDescriptionChange = onDescriptionChange
        )

        PeriodicityFields(
            getHabitState = getHabitState,
            getPeriodicityCountIsCorrect = getPeriodicityCountIsCorrect,
            getPeriodicityDaysCountIsCorrect = getPeriodicityDaysCountIsCorrect,
            onPeriodicityCountChange = onPeriodicityCountChange,
            onPeriodicityDaysCountChange = onPeriodicityDaysCountChange
        )

        PriorityField(
            getHabitState = getHabitState,
            onPriorityChange = onPriorityChange
        )

        ColorChooser(
            getHabitState = getHabitState,
            getAllowedColor = getAllowedColor,
            onColorChange = onColorChange
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
private fun Title() {
    Text(
        text = stringResource(R.string.CreateOrEditHabit),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        fontSize = 28.sp
    )
}

@Composable
private fun NameField(
    getHabitState: () -> CreateHabitState,
    getNameIsCorrect: () -> Boolean,
    onNameChange: (String) -> Unit
) {
    TextField(
        value = getHabitState().name,
        onValueChange = onNameChange,
        label = { Text(text = stringResource(R.string.HabitName)) },
        colors = TextFieldDefaults.colors(errorContainerColor = Color.Red),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        isError = !getNameIsCorrect()
    )
}


@Composable
fun DescriptionField(
    getHabitState: () -> CreateHabitState,
    onDescriptionChange: (String) -> Unit
) {
    TextField(
        value = getHabitState().description,
        label = { Text(text = stringResource(R.string.HabitDescription)) },
        onValueChange = onDescriptionChange,
        singleLine = false,
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(vertical = 10.dp),
    )
}

@Composable
private fun PeriodicityFields(
    getHabitState: () -> CreateHabitState,
    getPeriodicityCountIsCorrect: () -> Boolean,
    getPeriodicityDaysCountIsCorrect: () -> Boolean,
    onPeriodicityCountChange: (String) -> Unit,
    onPeriodicityDaysCountChange: (String) -> Unit,
) {
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
            value = getHabitState().priority.toString(),
            onValueChange = {},
            enabled = false,
            modifier = Modifier
                .padding(top = 15.dp)
                .clickable { expanded = !expanded },
            colors = TextFieldDefaults.colors(
                disabledTextColor = colorResource(id = R.color.TextField_TextColor)
            ),
            label = { Text(stringResource(R.string.ChoseHabitType)) },
            trailingIcon = { IconForDropdownMenu { expanded } }
        )

        DropdownPriorityMenu(
            getExpended = { expanded },
            hidingAction = { expanded = false },
            onPriorityChange = onPriorityChange
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
    onPriorityChange: (HabitPriority) -> Unit
) {
    DropdownMenu(
        expanded = getExpended(),
        onDismissRequest = hidingAction
    ) {
        HabitPriority.entries.forEach {
            DropdownMenuItem(
                text = { Text(text = it.toString()) },
                onClick = {
                    onPriorityChange(it)
                    hidingAction()
                }
            )
        }
    }
}

@Composable
private fun TypeButtons(
    getHabitState: () -> CreateHabitState,
    onTypeChange: (HabitType) -> Unit
) {
    Text(
        text = stringResource(R.string.ChoseHabitType),
        modifier = Modifier.padding(top = 20.dp)
    )

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
    Text(
        text = stringResource(R.string.HabitColor),
        modifier = Modifier.padding(top = 20.dp)
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        items(getAllowedColor()) {
            ColorDrawer(
                color = it,
                getHabitState = getHabitState,
                onColorChange = onColorChange
            )
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}

@Composable
private fun ColorDrawer(
    color: Color,
    getHabitState: () -> CreateHabitState,
    onColorChange: (Color) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(color)
            .clickable { onColorChange(color) }
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