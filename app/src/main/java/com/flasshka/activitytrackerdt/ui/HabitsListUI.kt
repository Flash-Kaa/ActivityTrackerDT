package com.flasshka.activitytrackerdt.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.flasshka.activitytrackerdt.R
import com.flasshka.activitytrackerdt.models.habit.Habit
import com.flasshka.activitytrackerdt.models.habit.HabitType
import com.flasshka.activitytrackerdt.ui.navigation.NavScreen
import com.flasshka.activitytrackerdt.viewmodels.MainVM
import kotlinx.coroutines.launch

private val prioritySize = 70.dp
private val priorityCircleRadius = 30.dp

private var habitType: HabitType by mutableStateOf(HabitType.NEW_SKILL)

private var habitNameFilter: String by mutableStateOf("")
private var habitNewDateSort: Boolean by mutableStateOf(true)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigateDrawer(
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        drawerBackgroundColor = colorResource(id = R.color.ScaffoldBG),
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            DrawerContent(navController)
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                        )
                    }
                },
            )
        },
        drawerShape = with(LocalDensity.current) {
            customDrawerShape(LocalConfiguration.current.screenHeightDp.dp.toPx())
        },
        content = {
            content(it)
        }
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitsListWithBottomSheet(
    padding: PaddingValues,
    navControllerScreen: NavHostController,
    vm: MainVM
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = colorResource(id = R.color.BottomSheetBG),
        sheetShape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
        backgroundColor = MaterialTheme.colorScheme.background,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            CreateButton(navControllerScreen = navControllerScreen)
        },
        sheetContent = {
            Box(
                modifier = Modifier
                    .padding(top = 60.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    androidx.compose.material3.TextField(
                        value = habitNameFilter,
                        onValueChange = {
                            habitNameFilter = it
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
                            Text(stringResource(R.string.findByName))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = ""
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(35.dp))

                    Text(text = stringResource(R.string.sortByDate))

                    Row(
                        modifier = Modifier
                            .padding(start = 60.dp, top = 10.dp, end = 30.dp)
                            .clickable {
                                habitNewDateSort = true
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = habitNewDateSort,
                            onClick = {
                                habitNewDateSort = true
                            }
                        )
                        Text(
                            text = stringResource(R.string.sortByNewDate),
                            textAlign = TextAlign.Center,
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(start = 60.dp, bottom = 35.dp)
                            .clickable {
                                habitNewDateSort = false
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = !habitNewDateSort,
                            onClick = {
                                habitNewDateSort = false
                            }
                        )
                        Text(
                            text = stringResource(R.string.sortByOldestDate),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        },
    ) {
        val filtered = vm.habits.filter {
            it.type == habitType && it.name.contains(habitNameFilter, true)
        }

        Column {
            TextTabs()

            if (filtered.isEmpty()) {
                EmptyList()
            } else {
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(filtered.sortedBy { if (habitNewDateSort) it.date else -it.date }) { habit ->
                        Habit(habit, navControllerScreen)
                        DistanceBetweenHabits()
                    }
                }
            }
        }
    }
}

@Composable
fun TextTabs() {
    var tabIndex: Int by remember {
        mutableIntStateOf(HabitType.entries.indexOf(habitType))
    }

    TabRow(selectedTabIndex = tabIndex) {
        HabitType.entries.forEachIndexed { index, itHabitType ->
            Tab(
                selected = tabIndex == index,
                onClick = {
                    tabIndex = index
                    habitType = itHabitType
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
private fun DrawerContent(navController: NavController) {
    Column {
        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(15.dp)
                .fillMaxWidth(),
            fontSize = 28.sp
        )

        TextButton(stringResource(R.string.habit_title)) {
            navController.navigate(NavScreen.MainScreen.ListOfHabits.route)
        }
        TextButton(stringResource(R.string.info_title)) {
            navController.navigate(NavScreen.MainScreen.InfoAboutApp.route)
        }
    }
}

@Composable
fun TextButton(
    text: String,
    action: () -> Unit
) {
    Text(
        text = text,
        fontSize = 24.sp,
        modifier = Modifier
            .background(colorResource(id = R.color.ScaffoldBG))
            .clickable(onClick = action)
            .fillMaxWidth()
            .padding(15.dp)
    )
}

private fun customDrawerShape(height: Float) = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rounded(
            RoundRect(
                left = 0f,
                top = 0f,
                right = 900f,
                bottom = height,
                topRightCornerRadius = CornerRadius(x = 90f, y = 90f)
            )
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
private fun CreateButton(navControllerScreen: NavHostController) {
    Box(
        contentAlignment = Alignment.BottomCenter
    ) {
        SmallFloatingActionButton(
            modifier = Modifier.size(80.dp),
            shape = RoundedCornerShape(40.dp),
            onClick = {
                navControllerScreen.navigate(NavScreen.CreateHabitScreen.route)
            }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_add_120),
                contentDescription = "add_button"
            )
        }
    }
}

@Composable
private fun Habit(
    habit: Habit,
    navControllerScreen: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navControllerScreen.navigate("${NavScreen.CreateHabitScreen.route}/${habit.id}")
            }
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
                text = "(${habit.periodicity})",
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${stringResource(id = R.string.HabitPriority)}: ${habit.priority}",
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${stringResource(id = R.string.HabitType)}: ${habit.type}",
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