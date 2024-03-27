package com.flasshka.activitytrackerdt.ui

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import com.flasshka.activitytrackerdt.models.habit.Habit
import com.flasshka.activitytrackerdt.models.habit.HabitPeriodicity
import com.flasshka.activitytrackerdt.models.habit.HabitPriority
import com.flasshka.activitytrackerdt.models.habit.HabitType
import com.flasshka.activitytrackerdt.ui.navigation.NavScreen
import com.flasshka.activitytrackerdt.viewmodels.MainVM
import java.time.LocalDateTime
import java.time.ZoneOffset

private var closeButtonEnabled: Boolean by mutableStateOf(false)

private var name: String by mutableStateOf("")
private var description: String by mutableStateOf("")
private var priority: HabitPriority by mutableStateOf(HabitPriority.URGENT_AND_IMPORTANT)
private var type: HabitType by mutableStateOf(HabitType.NEW_SKILL)

private var periodicityCount: Int by mutableIntStateOf(0)
private var periodicityDays: Int by mutableIntStateOf(0)
private var color: Color by mutableStateOf(Color.Black)

private var id: Long by mutableLongStateOf(-1L)

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

private fun closeButtonEnabled() =
    name.length >= 3 && periodicityDays > 0 && periodicityCount > 0

private var lastChangeId: Long? = null

@Composable
fun CreateHabitUI(
    vm: MainVM,
    navController: NavController,
    habitIdToChange: Long? = null
) {
    if (habitIdToChange != lastChangeId) {
        lastChangeId = habitIdToChange

        id = -1L
        name = ""
        description = ""
        priority = HabitPriority.URGENT_AND_IMPORTANT
        type = HabitType.NEW_SKILL
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
    }

    closeButtonEnabled = closeButtonEnabled()

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
            value = name,
            onValueChange = {
                name = it
                closeButtonEnabled = closeButtonEnabled()
                isError = name.length < 3
            },
            label = { Text(stringResource(R.string.HabitName)) },
            colors = TextFieldDefaults.colors(errorContainerColor = Color.Red),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            isError = isError
        )

        TextField(
            value = description,
            label = {
                Text(stringResource(R.string.HabitDescription))
            },
            onValueChange = {
                description = it
            },
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
                getValue = { periodicityDays }
            ) {
                periodicityDays = it
            }

            PeriodicityField(
                text = stringResource(R.string.PeriodicityInCount),
                getValue = { periodicityCount }
            ) {
                periodicityCount = it
            }
        }

        PriorityField()

        Text(
            text = stringResource(R.string.HabitColor),
            modifier = Modifier.padding(top = 20.dp)
        )

        ColorChooser()

        Text(
            text = stringResource(R.string.HabitType),
            modifier = Modifier.padding(top = 20.dp)
        )

        TypeButtons()
    }

    SaveButton(vm, navController)
}

@Composable
private fun SaveButton(
    vm: MainVM,
    navController: NavController
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
            onClick = {
                val habit = Habit(
                    id, name, description, priority, type,
                    HabitPeriodicity(periodicityCount, periodicityDays), color,
                    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
                )

                id = -1L
                name = ""
                description = ""
                priority = HabitPriority.URGENT_AND_IMPORTANT
                type = HabitType.NEW_SKILL
                periodicityCount = 0
                periodicityDays = 0
                color = Color.Black

                vm.addOrUpdate(habit)
                navController.navigate(NavScreen.MainScreen.route)
            },
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

            closeButtonEnabled = closeButtonEnabled()
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
private fun PriorityField() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(priority.toString()) }

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
                        selectedOption = it.toString()
                        priority = it
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun TypeButtons() {
    HabitType.entries.forEach {
        Row(
            modifier = Modifier
                .clickable { type = it }
                .size(width = 200.dp, height = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = type == it,
                onClick = { type = it }
            )

            Text(
                text = it.toString(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ColorChooser() {
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
