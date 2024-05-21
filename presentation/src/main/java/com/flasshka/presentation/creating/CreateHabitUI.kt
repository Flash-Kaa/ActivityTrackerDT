package com.flasshka.presentation.creating

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasshka.presentation.R
import com.flasshka.presentation.TestTags

@Composable
fun DrawCreateHabitUI(
    vm: CreateHabitVM,
    habitIdToChange: String? = null
) {
    CreateHabitUI(
        colors = vm.listOfAllowedColors,
        getHabitState = vm.getHabitStateAction(habitIdToChange),
        getNameIsCorrect = vm::getNameIsCorrectAction,
        getPeriodicityCountIsCorrect = vm::getPeriodicityCountIsCorrectAction,
        getPeriodicityDaysCountIsCorrect = vm::getPeriodicityDaysCountIsCorrectAction,
        getDescriptionIsCorrect = vm::getDescriptionIsCorrectAction,
        getSaveButtonIsEnabled = vm::getSaveButtonIsEnabled,
        getAction = vm::getAction
    )
}

@Composable
private fun CreateHabitUI(
    colors: List<Color>,
    getHabitState: () -> CreateHabitState,
    getNameIsCorrect: () -> Boolean,
    getPeriodicityCountIsCorrect: () -> Boolean,
    getPeriodicityDaysCountIsCorrect: () -> Boolean,
    getDescriptionIsCorrect: () -> Boolean,
    getSaveButtonIsEnabled: () -> Boolean,
    getAction: (CreateHabitActionType) -> (() -> Unit)
) {
    LazyColumn(
        modifier = Modifier.padding(10.dp),
    ) {
        item {
            Title()
        }

        item {
            NameField(
                getHabitState = getHabitState,
                getNameIsCorrect = getNameIsCorrect,
                getAction = getAction
            )
        }

        item {
            DescriptionField(
                getHabitState = getHabitState,
                getDescriptionIsCorrect = getDescriptionIsCorrect,
                getAction = getAction
            )
        }

        item {
            PeriodicityFields(
                getHabitState = getHabitState,
                getPeriodicityCountIsCorrect = getPeriodicityCountIsCorrect,
                getPeriodicityDaysCountIsCorrect = getPeriodicityDaysCountIsCorrect,
                getAction = getAction
            )
        }

        item {
            PriorityField(
                getHabitState = getHabitState,
                getAction = getAction
            )
        }

        item {
            ColorChooser(
                getHabitState = getHabitState,
                colors = colors,
                getAction = getAction
            )
        }

        item {
            TypeButtons(
                getHabitState = getHabitState,
                getAction = getAction
            )
        }
    }

    SaveButton(
        getSaveButtonIsEnabled = getSaveButtonIsEnabled,
        getAction = getAction
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
    getAction: (CreateHabitActionType) -> (() -> Unit)
) {
    TextField(
        value = getHabitState().name,
        onValueChange = {
            getAction(CreateHabitActionType.OnNameChange(it)).invoke()
        },
        label = { Text(text = stringResource(R.string.HabitName)) },
        colors = TextFieldDefaults.colors(errorContainerColor = Color.Red),
        modifier = Modifier
            .testTag(TestTags.NAME_FIELD.toString())
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        isError = !getNameIsCorrect()
    )
}


@Composable
private fun DescriptionField(
    getHabitState: () -> CreateHabitState,
    getDescriptionIsCorrect: () -> Boolean,
    getAction: (CreateHabitActionType) -> (() -> Unit)
) {
    TextField(
        value = getHabitState().description,
        label = { Text(text = stringResource(R.string.HabitDescription)) },
        isError = !getDescriptionIsCorrect(),
        onValueChange = {
            getAction(CreateHabitActionType.OnDescriptionChange(it)).invoke()
        },
        singleLine = false,
        colors = TextFieldDefaults.colors(errorContainerColor = Color.Red),
        modifier = Modifier
            .testTag(TestTags.DESCR_FIELD.toString())
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
    getAction: (CreateHabitActionType) -> (() -> Unit)
) {
    Text(
        text = stringResource(R.string.Periodicity),
        modifier = Modifier.padding(top = 15.dp)
    )

    Row {
        PeriodicityField(
            text = stringResource(R.string.PeriodicityInDays),
            getValue = { getHabitState().periodicityDaysCount },
            isCorrect = getPeriodicityDaysCountIsCorrect,
            modifier = Modifier.testTag(TestTags.PERIOD_DAYS_FIELD.toString()),
            onValueChange = {
                getAction(CreateHabitActionType.OnPeriodicityDaysCountChange(it)).invoke()
            }
        )

        PeriodicityField(
            text = stringResource(R.string.PeriodicityInCount),
            getValue = { getHabitState().periodicityCount },
            isCorrect = getPeriodicityCountIsCorrect,
            modifier = Modifier.testTag(TestTags.PERIOD_COUNT_FIELD.toString()),
            onValueChange = {
                getAction(CreateHabitActionType.OnPeriodicityCountChange(it)).invoke()
            }
        )
    }
}

@Composable
private fun PeriodicityField(
    text: String,
    modifier: Modifier = Modifier,
    getValue: () -> String,
    isCorrect: () -> Boolean,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = getValue(),
        label = { Text(text) },
        onValueChange = onValueChange,
        modifier = modifier
            .padding(10.dp)
            .width(150.dp),
        isError = !isCorrect(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        colors = TextFieldDefaults.colors(errorContainerColor = Color.Red)
    )
}