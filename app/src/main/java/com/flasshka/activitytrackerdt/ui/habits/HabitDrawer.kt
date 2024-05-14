package com.flasshka.activitytrackerdt.ui.habits

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasshka.activitytrackerdt.R
import com.flasshka.activitytrackerdt.models.habit.Habit

@Composable
fun HabitDrawer(
    habit: Habit,
    getAction: (HabitListActionType) -> (() -> Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(
                onClick = getAction(HabitListActionType.NavigateToChangeHabit(habit.uid))
            )
            .background(colorResource(id = R.color.HabitBG))
            .padding(horizontal = 12.dp)
    ) {
        ColorDrawer(color = habit.color)

        Column {
            Text(
                text = habit.name,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(id = R.string.InBrackets, habit.periodicity.toString()),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(id = R.string.HabitPriority, habit.priority.toString()),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(id = R.string.HabitType, habit.type.toString()),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = habit.description,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ColorDrawer(color: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(70.dp)
    ) {
        Canvas(modifier = Modifier) {
            drawCircle(
                color = color,
                radius = 30.dp.toPx()
            )
        }
    }
}