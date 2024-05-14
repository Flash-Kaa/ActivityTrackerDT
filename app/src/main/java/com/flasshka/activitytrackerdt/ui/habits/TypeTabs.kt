package com.flasshka.activitytrackerdt.ui.habits

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flasshka.activitytrackerdt.models.habit.HabitType
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TypeTabs(
    pagerState: PagerState,
    indexOfChosenFilterByHabitType: Int,
    getAction: (HabitListActionType) -> (() -> Unit)
) {
    val scope = rememberCoroutineScope()

    TabRow(indexOfChosenFilterByHabitType) {
        HabitType.entries.forEachIndexed { index, itHabitType ->
            Tab(
                selected = indexOfChosenFilterByHabitType == index,
                onClick = {
                    getAction(HabitListActionType.ChangeIndexOfChosenType(index)).invoke()
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            ) {
                Text(
                    text = itHabitType.toString(),
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}