package com.flasshka.activitytrackerdt.ui.habits

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Tab
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.flasshka.activitytrackerdt.R
import com.flasshka.activitytrackerdt.models.habit.Habit
import com.flasshka.activitytrackerdt.models.habit.HabitType
import com.flasshka.activitytrackerdt.ui.MainVM
import kotlinx.coroutines.launch

@Composable
fun DrawerHabitsListWithBottomSheet(
    vm: MainVM,
    padding: PaddingValues,
    navController: NavController
) {
    HabitsListWithBottomSheet(
        padding = padding,
        getSortedAndFilteredHabitList = vm.getSortedAndFilteredHabitList(),
        getCurrentValueFilterName = vm.getCurrentValueHabitNameFilter(),
        getCurrentValueSortHabitsByDateFromNew = vm.getCurrentValueSortHabitsByDate(),
        getCurrentIndexOfChosenFilterByHabitType = vm.getCurrentIndexOfChosenFilterByHabitType(),
        changeIndexOfChosenFilterByHabitTypeAction = vm.getChangeIndexOfChosenFilterByHabitTypeAction(),
        habitNameFilterAction = vm.getUpdateHabitNameFilterAction(),
        sortHabitsByDateFromNewAction = vm.getSortHabitsByDateAction(),
        navigateToChangeHabitAction = vm.getNavigateToChangeHabitAction(navController),
        navigateToCreateHabitAction = vm.getNavigateToCreateHabitAction(navController),
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HabitsListWithBottomSheet(
    padding: PaddingValues,
    getCurrentValueFilterName: () -> String,
    getCurrentIndexOfChosenFilterByHabitType: () -> Int,
    getCurrentValueSortHabitsByDateFromNew: () -> Boolean,
    getSortedAndFilteredHabitList: () -> List<Habit>,
    sortHabitsByDateFromNewAction: (Boolean) -> (() -> Unit),
    changeIndexOfChosenFilterByHabitTypeAction: (Int) -> (() -> Unit),
    habitNameFilterAction: (String) -> Unit,
    navigateToChangeHabitAction: (Long) -> (() -> Unit),
    navigateToCreateHabitAction: () -> Unit
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = colorResource(id = R.color.BottomSheetBG),
        sheetShape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
        backgroundColor = MaterialTheme.colorScheme.background,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = { CreateButton(navigateToCreateHabitAction) },
        sheetContent = {
            SheetContent(
                getCurrentValueFilterName = getCurrentValueFilterName,
                getCurrentValueSortHabitsByDateFromNew = getCurrentValueSortHabitsByDateFromNew,
                sortHabitsByDateFromNewAction = sortHabitsByDateFromNewAction,
                habitNameFilterAction = habitNameFilterAction
            )
        },
        content = {
            Content(
                padding = padding,
                getCurrentIndexOfChosenFilterByHabitType = getCurrentIndexOfChosenFilterByHabitType,
                getSortedAndFilteredHabitList = getSortedAndFilteredHabitList,
                changeIndexOfChosenFilterByHabitTypeAction = changeIndexOfChosenFilterByHabitTypeAction,
                navigateToChangeHabitAction = navigateToChangeHabitAction
            )
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    padding: PaddingValues,
    getCurrentIndexOfChosenFilterByHabitType: () -> Int,
    getSortedAndFilteredHabitList: () -> List<Habit>,
    changeIndexOfChosenFilterByHabitTypeAction: (Int) -> (() -> Unit),
    navigateToChangeHabitAction: (Long) -> (() -> Unit),
) {
    val pagerState = rememberPagerState { HabitType.entries.size }
    val scope = rememberCoroutineScope()

    Column {
        TextTabs(
            pagerState = pagerState,
            getCurrentIndexOfChosenFilterByHabitType = getCurrentIndexOfChosenFilterByHabitType,
            changeIndexOfChosenFilterByHabitTypeAction = changeIndexOfChosenFilterByHabitTypeAction
        )

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            changeIndexOfChosenFilterByHabitTypeAction(pagerState.targetPage).invoke()
            if (getSortedAndFilteredHabitList().isEmpty()) {
                EmptyList()
            } else {
                LazyColumn(
                    modifier = Modifier.padding(padding)
                ) {
                    items(getSortedAndFilteredHabitList()) { habit ->
                        HabitDrawer(habit, navigateToChangeHabitAction)
                        DistanceBetweenHabits()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TextTabs(
    pagerState: PagerState,
    getCurrentIndexOfChosenFilterByHabitType: () -> Int,
    changeIndexOfChosenFilterByHabitTypeAction: (Int) -> (() -> Unit)
) {
    val scope = rememberCoroutineScope()

    TabRow(getCurrentIndexOfChosenFilterByHabitType()) {
        HabitType.entries.forEachIndexed { index, itHabitType ->
            Tab(
                selected = getCurrentIndexOfChosenFilterByHabitType() == index,
                onClick = {
                    changeIndexOfChosenFilterByHabitTypeAction(index).invoke()
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

@Composable
private fun SheetContent(
    getCurrentValueFilterName: () -> String,
    getCurrentValueSortHabitsByDateFromNew: () -> Boolean,
    sortHabitsByDateFromNewAction: (Boolean) -> (() -> Unit),
    habitNameFilterAction: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 60.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = getCurrentValueFilterName(),
            onValueChange = habitNameFilterAction,
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
            newValueSortHabitsByDateFromNew = true,
            getCurrentValueSortHabitsByDateFromNew = getCurrentValueSortHabitsByDateFromNew,
            sortFromNewAction = sortHabitsByDateFromNewAction
        )

        RowRadioButton(
            text = stringResource(R.string.SortByNewDate),
            newValueSortHabitsByDateFromNew = false,
            getCurrentValueSortHabitsByDateFromNew = { !getCurrentValueSortHabitsByDateFromNew() },
            sortFromNewAction = sortHabitsByDateFromNewAction
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
private fun RowRadioButton(
    text: String,
    newValueSortHabitsByDateFromNew: Boolean,
    getCurrentValueSortHabitsByDateFromNew: () -> Boolean,
    sortFromNewAction: (Boolean) -> (() -> Unit),
) {
    Row(
        modifier = Modifier
            .padding(start = 30.dp)
            .clickable(onClick = sortFromNewAction(newValueSortHabitsByDateFromNew)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = getCurrentValueSortHabitsByDateFromNew(),
            onClick = sortFromNewAction(newValueSortHabitsByDateFromNew)
        )

        Text(
            text = text,
            textAlign = TextAlign.Center,
        )
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
            contentDescription = "arror to create"
        )
    }
}

@Composable
private fun CreateButton(
    navigateToCreateHabitAction: () -> Unit
) {
    Box(contentAlignment = Alignment.BottomCenter) {
        SmallFloatingActionButton(
            modifier = Modifier.size(80.dp),
            shape = RoundedCornerShape(40.dp),
            onClick = navigateToCreateHabitAction
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_add_120),
                contentDescription = "add_button"
            )
        }
    }
}

@Composable
private fun HabitDrawer(
    habit: Habit,
    navigateToChangeHabitAction: (Long) -> (() -> Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = navigateToChangeHabitAction(habit.id))
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
private fun DistanceBetweenHabits() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(15.dp)
    ) {
        Canvas(modifier = Modifier) {
            drawLine(
                color = Color.Red,
                Offset(Float.MIN_VALUE, 0f),
                Offset(Float.MAX_VALUE, 0f)
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