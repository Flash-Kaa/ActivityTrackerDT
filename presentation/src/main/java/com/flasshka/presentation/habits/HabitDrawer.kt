package com.flasshka.presentation.habits

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasshka.presentation.R
import com.flasshka.presentation.TestTags

@Composable
fun HabitDrawer(
    habit: com.flasshka.domain.entities.Habit,
    getAction: (HabitListActionType) -> (() -> Unit)
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(
                onClick = getAction(HabitListActionType.NavigateToChangeHabit(habit.uid))
            )
            .background(colorResource(id = R.color.HabitBG), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp)
    ) {

        Row(Modifier.fillMaxWidth()) {
            ColorDrawer(color = Color(habit.color))

            Column {
                Text(
                    text = habit.name,
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                TagWithShape(habit.periodicity.toString())
                TagWithShape(habit.priority.toString())
            }
        }

        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = habit.description,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    colorResource(id = R.color.DescriptionBackground),
                    RoundedCornerShape(15.dp)
                )
                .padding(7.dp)
        )

        ButtonRow(
            habit = habit,
            getAction = getAction
        )
    }
}

@Composable
fun ButtonRow(
    habit: com.flasshka.domain.entities.Habit,
    getAction: (HabitListActionType) -> (() -> Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = getAction(HabitListActionType.CompleteHabit(habit)),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(10.dp)
        ) {
            Text(text = stringResource(R.string.complited))
        }

        var openDialog: Boolean by remember { mutableStateOf(false) }

        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Button(
                onClick = { openDialog = true },
                modifier = Modifier
                    .testTag(TestTags.DELETE_BUTTON.toString())
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_delete_24),
                    contentDescription = ""
                )
            }
        }

        if (openDialog) {
            AlertDialog(
                onDismissRequest = { openDialog = false },
                confirmButton = {
                    Button(
                        modifier = Modifier.testTag(TestTags.DONE_DELETE_BUTTON.toString()),
                        onClick = {
                            openDialog = false
                            getAction(HabitListActionType.DeleteHabit(habit)).invoke()
                        }
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                },
                dismissButton = {
                    Button(onClick = { openDialog = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}

@Composable
private fun ColorDrawer(color: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(top = 65.dp, start = 45.dp)
    ) {
        Canvas(modifier = Modifier) {
            drawCircle(
                color = color,
                radius = 40.dp.toPx()
            )
        }
    }
}

@Composable
private fun TagWithShape(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.TagsWithShape),
            modifier = Modifier
                .padding(3.dp)
                .border(2.dp, colorResource(id = R.color.TagsWithShape), CircleShape)
                .padding(7.dp)
        )
    }
}