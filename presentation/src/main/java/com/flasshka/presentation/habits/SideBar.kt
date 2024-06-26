package com.flasshka.presentation.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flasshka.presentation.MainVM
import com.flasshka.presentation.R
import com.flasshka.presentation.TestTags
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerSideBar(
    vm: MainVM,
    content: @Composable (PaddingValues) -> Unit
) {
    SideBar(
        getAction = vm::getAction,
        content = content
    )
}

@Composable
private fun SideBar(
    getAction: (HabitListActionType) -> (() -> Unit),
    content: @Composable (PaddingValues) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    androidx.compose.material.Scaffold(
        drawerBackgroundColor = colorResource(id = R.color.ScaffoldBG),
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            DrawerContent(
                getAction = getAction
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBarDrawer(
                scope = scope,
                scaffoldState = scaffoldState
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
private fun DrawerContent(
    getAction: (HabitListActionType) -> (() -> Unit)
) {
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

        Avatar()

        TextButton(
            text = stringResource(R.string.Habit_title),
            modifier = Modifier.testTag(TestTags.OPEN_LIST_BUTTON.toString()),
            navigateAction = getAction(HabitListActionType.NavigateToListOfHabits)
        )

        TextButton(
            text = stringResource(R.string.Info_title),
            modifier = Modifier.testTag(TestTags.OPEN_INFO_BUTTON.toString()),
            navigateAction = getAction(HabitListActionType.NavigateToInfo)
        )
    }
}

@Composable
private fun TextButton(
    text: String,
    modifier: Modifier = Modifier,
    navigateAction: () -> Unit
) {
    Text(
        text = text,
        fontSize = 24.sp,
        modifier = modifier
            .background(colorResource(id = R.color.ScaffoldBG))
            .clickable(onClick = navigateAction)
            .fillMaxWidth()
            .padding(15.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarDrawer(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.testTag(TestTags.OPEN_SIDE_BAR_BUTTON.toString()),
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
}