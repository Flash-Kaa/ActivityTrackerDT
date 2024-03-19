package com.flasshka.activitytrackerdt.ui

import android.app.Activity
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
import com.flasshka.activitytrackerdt.HabitPriority
import com.flasshka.activitytrackerdt.HabitType
import com.flasshka.activitytrackerdt.R
import com.flasshka.activitytrackerdt.viewmodels.CreateHabitVM

private var closeButtonEnabled: Boolean by mutableStateOf(false)

private fun closeButtonEnabled(vm: CreateHabitVM) =
    vm.name.length >= 3 && vm.periodicityDays > 0 && vm.periodicityCount > 0

private val allowedColorList = listOf(
    Color.Black,
    Color.Gray,
    Color(192, 192, 192),
    Color.White,
    Color(255, 0, 255),
    Color(128, 0, 128),
    Color.Red,
    Color(128, 0, 0),
    Color.Yellow,
    Color(128, 128, 0),
    Color(0, 255, 0),
    Color(0, 128, 0),
    Color(0, 255, 255),
    Color(0, 128, 128),
    Color.Blue,
    Color(0, 0, 128)
)

@Composable
fun Fields(vm: CreateHabitVM) {
    closeButtonEnabled = closeButtonEnabled(vm)

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

        var isError: Boolean by remember {
            mutableStateOf(false)
        }

        TextField(
            value = vm.name,
            onValueChange = {
                vm.name = it
                closeButtonEnabled = closeButtonEnabled(vm)
                isError = vm.name.length < 3
            },
            label = { Text(stringResource(R.string.HabitName)) },
            colors = TextFieldDefaults.colors(errorContainerColor = Color.Red),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            isError = isError
        )

        TextField(
            value = vm.description,
            label = { Text(stringResource(R.string.HabitDescription)) },
            onValueChange = { vm.description = it },
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
                vm = vm,
                text = stringResource(R.string.PeriodicityInDays),
                getValue = { vm.periodicityDays }
            ) {
                vm.periodicityDays = it
            }

            PeriodicityField(
                vm = vm,
                text = stringResource(R.string.PeriodicityInCount),
                getValue = { vm.periodicityCount }
            ) {
                vm.periodicityCount = it
            }
        }

        PriorityField(vm)

        Text(
            text = stringResource(R.string.HabitColor),
            modifier = Modifier.padding(top = 20.dp)
        )

        ColorChooser(vm)

        Text(
            text = stringResource(R.string.HabitType),
            modifier = Modifier.padding(top = 20.dp)
        )

        TypeButtons(vm)
    }
}

@Composable
fun SaveButton(activity: Activity, vm: CreateHabitVM) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Button(
            modifier = Modifier
                .padding(10.dp)
                .size(80.dp),
            shape = RoundedCornerShape(40.dp),
            onClick = { vm.save(activity) },
            enabled = closeButtonEnabled
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
    vm: CreateHabitVM,
    text: String,
    getValue: () -> Int,
    setValue: (Int) -> Unit,
) {
    var error: Boolean by remember {
        mutableStateOf(false)
    }

    var periodicity: String by remember {
        val value = getValue()
        mutableStateOf(if (value > 0) value.toString() else "")
    }

    TextField(
        value = periodicity,
        label = { Text(text) },
        onValueChange = {
            val toInt = it.toIntOrNull()

            error = if (toInt != null && toInt > 0) {
                setValue(toInt)
                false
            } else {
                true
            }

            closeButtonEnabled = closeButtonEnabled(vm)
            periodicity = it
        },
        modifier = Modifier
            .padding(10.dp)
            .width(150.dp),
        isError = error,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        colors = TextFieldDefaults.colors(errorContainerColor = Color.Red)
    )
}

@Composable
private fun PriorityField(vm: CreateHabitVM) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(vm.priority.toString()) }

    Box {
        TextField(
            value = selectedOption,
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
                disabledTextColor = colorResource(id = R.color.PriorityFieldText)
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
                        selectedOption = it.toString()
                        vm.priority = it
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun TypeButtons(vm: CreateHabitVM) {
    HabitType.entries.forEach {
        Row(
            modifier = Modifier
                .clickable { vm.type = it }
                .size(width = 200.dp, height = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = vm.type == it,
                onClick = { vm.type = it }
            )

            Text(
                text = it.toString(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ColorChooser(vm: CreateHabitVM) {
    var color: Color by remember {
        mutableStateOf(vm.color)
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        items(allowedColorList) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(it)
                    .clickable {
                        color = it
                        vm.color = it
                    }
                    .size(40.dp)
            ) {
                if (it == color) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.round_done_36),
                        tint = Color(75, 0, 130),
                        contentDescription = "selected"
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(20.dp)
            )
        }
    }
}
