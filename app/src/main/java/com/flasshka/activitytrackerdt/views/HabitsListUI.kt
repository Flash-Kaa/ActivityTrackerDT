package com.flasshka.activitytrackerdt.views

import android.app.Activity
import android.util.DisplayMetrics
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasshka.activitytrackerdt.Habit
import com.flasshka.activitytrackerdt.R
import com.flasshka.activitytrackerdt.viewmodels.MainVM


class HabitsListUI(private val vm: MainVM) {
    private val prioritySize = 70.dp
    private val priorityCircleRadius = 30.dp

    private val lineBetweenHabitsOffset = 50.dp

    @Composable
    fun HabitsList(habits: List<Habit>, activity: Activity) {
        LazyColumn(
            modifier = Modifier.padding(15.dp)
        ) {
            items(habits) {
                Habit(it, activity)
                DistanceBetweenHabits(activity)
            }
        }
    }

    @Composable
    fun EmptyList() {
        Column(
            modifier = Modifier.padding(top = 230.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Создай привычку",
                textAlign = TextAlign.Center,
                lineHeight = 64.sp,
                fontSize = 64.sp
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_arrow_downward_240),
                modifier = Modifier.padding(top = 80.dp),
                contentDescription = "arror to create"
            )
        }
    }

    @Composable
    fun CreateButton(activity: Activity) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SmallFloatingActionButton(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(50.dp),
                onClick = { vm.toCreateActivity(activity) }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_add_120),
                    contentDescription = "add_button"
                )
            }
        }
    }

    @Composable
    private fun Habit(habit: Habit, activity: Activity) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    vm.toCreateActivity(activity, habit)
                }
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
                    text = "(${habit.periodicity})",
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Приоритет: ${habit.priority}",
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Тип: ${habit.type}",
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
    private fun DistanceBetweenHabits(activity: Activity) {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(15.dp)
        ) {
            Canvas(modifier = Modifier) {
                drawLine(
                    color = Color.Red,
                    Offset(lineBetweenHabitsOffset.toPx(), 0f),
                    Offset(displayMetrics.widthPixels - lineBetweenHabitsOffset.toPx(), 0f)
                )
            }
        }
    }

    @Composable
    private fun ColorDrawer(
        color: Color
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(prioritySize)
        ) {
            Canvas(modifier = Modifier) {
                drawCircle(
                    color = color,
                    radius = priorityCircleRadius.toPx()
                )
            }
        }
    }
}