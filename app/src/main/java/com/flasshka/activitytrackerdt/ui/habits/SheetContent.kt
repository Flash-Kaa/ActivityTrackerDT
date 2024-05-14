package com.flasshka.activitytrackerdt.ui.habits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.flasshka.activitytrackerdt.R

@Composable
fun SheetContent(
    filterName: String,
    sortHabitsByDateFromOld: Boolean,
    getAction: (HabitListActionType) -> (() -> Unit)
) {
    Column(
        modifier = Modifier
            .padding(top = 60.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = filterName,
            onValueChange = {
                getAction(HabitListActionType.UpdateFilter(it)).invoke()
            },
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = colorResource(id = R.color.TextField_TextColor),
                unfocusedTextColor = colorResource(id = R.color.TextField_TextColor),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            label = {
                Text(text = stringResource(R.string.FindByName))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = ""
                )
            }
        )

        Spacer(modifier = Modifier.height(35.dp))
        Text(text = stringResource(R.string.SortByDate))
        Spacer(modifier = Modifier.height(15.dp))

        RowRadioButton(
            text = stringResource(R.string.SortByOldestDate),
            sortHabitsByDateFromOld = sortHabitsByDateFromOld,
            sortFromNewAction = getAction(HabitListActionType.SortByDate(fromNew = true))
        )

        RowRadioButton(
            text = stringResource(R.string.SortByNewDate),
            sortHabitsByDateFromOld = !sortHabitsByDateFromOld,
            sortFromNewAction = getAction(HabitListActionType.SortByDate(fromNew = false))
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
private fun RowRadioButton(
    text: String,
    sortHabitsByDateFromOld: Boolean,
    sortFromNewAction: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(start = 30.dp)
            .clickable(onClick = sortFromNewAction),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = sortHabitsByDateFromOld,
            onClick = sortFromNewAction
        )

        Text(
            text = text,
            textAlign = TextAlign.Center,
        )
    }
}