package com.flasshka.presentation.habits

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasshka.domain.entities.Habit
import com.flasshka.presentation.MainVM
import com.flasshka.presentation.R

@Composable
fun DrawerHabitsList(
    vm: MainVM,
    padding: PaddingValues
) {
    HabitsListWithBottomSheet(
        padding = padding,
        sortedAndFilteredHabitList = vm.getSortedAndFilteredHabitList(),
        filterName = vm.habitNameFilter,
        sortHabitsByDateFromOld = vm.sortHabitsByDateFromOld,
        indexOfChosenFilterByHabitType = vm.indexChosenFilterByHabitType,
        getAction = vm::getAction
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HabitsListWithBottomSheet(
    padding: PaddingValues,
    filterName: String,
    indexOfChosenFilterByHabitType: Int,
    sortHabitsByDateFromOld: Boolean,
    sortedAndFilteredHabitList: List<Habit>,
    getAction: (HabitListActionType) -> (() -> Unit)
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = colorResource(id = R.color.BottomSheetBG),
        sheetShape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
        backgroundColor = MaterialTheme.colorScheme.background,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = { CreateButton(getAction) },
        sheetContent = {
            SheetContent(
                filterName = filterName,
                sortHabitsByDateFromOld = sortHabitsByDateFromOld,
                getAction = getAction
            )
        },
        content = {
            HorizontalPage(
                padding = padding,
                indexOfChosenFilterByHabitType = indexOfChosenFilterByHabitType,
                sortedAndFilteredHabitList = sortedAndFilteredHabitList,
                getAction = getAction
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HorizontalPage(
    padding: PaddingValues,
    indexOfChosenFilterByHabitType: Int,
    sortedAndFilteredHabitList: List<com.flasshka.domain.entities.Habit>,
    getAction: (HabitListActionType) -> (() -> Unit)
) {
    val pagerState = rememberPagerState { Habit.Type.entries.size }

    Column {
        TypeTabs(
            pagerState = pagerState,
            indexOfChosenFilterByHabitType = indexOfChosenFilterByHabitType,
            getAction = getAction
        )

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            getAction(HabitListActionType.ChangeIndexOfChosenType(pagerState.targetPage)).invoke()
            if (sortedAndFilteredHabitList.isEmpty()) {
                EmptyList()
            } else {
                ListOfHabits(
                    padding = padding,
                    sortedAndFilteredHabitList = sortedAndFilteredHabitList,
                    getAction = getAction
                )
            }
        }
    }
}

@Composable
fun ListOfHabits(
    padding: PaddingValues,
    sortedAndFilteredHabitList: List<Habit>,
    getAction: (HabitListActionType) -> (() -> Unit)
) {
    LazyColumn(
        modifier = Modifier.padding(padding)
    ) {
        items(sortedAndFilteredHabitList) { habit ->
            HabitDrawer(habit, getAction)
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun EmptyList() {
    Column(
        modifier = Modifier.padding(top = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.CreateHabit),
            textAlign = TextAlign.Center,
            lineHeight = 64.sp,
            fontSize = 64.sp
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.baseline_arrow_downward_240),
            contentDescription = "arrow to create"
        )
    }
}