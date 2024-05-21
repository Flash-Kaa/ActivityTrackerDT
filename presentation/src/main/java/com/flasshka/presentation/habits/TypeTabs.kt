package com.flasshka.presentation.habits

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.flasshka.domain.entities.Habit
import com.flasshka.presentation.TestTags
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
        Habit.Type.entries.forEachIndexed { index, itHabitType ->
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
                    modifier = Modifier
                        .testTag(
                            if (itHabitType == Habit.Type.GOOD)
                                TestTags.GOOD_LIST .toString()
                            else
                                TestTags.BAD_LIST.toString())
                        .padding(5.dp)
                )
            }
        }
    }
}