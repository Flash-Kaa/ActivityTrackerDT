package com.flasshka.activitytrackerdt.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.flasshka.activitytrackerdt.ui.Info
import com.flasshka.activitytrackerdt.ui.MainVM
import com.flasshka.activitytrackerdt.ui.creating.CreateHabitVM
import com.flasshka.activitytrackerdt.ui.creating.DrawCreateHabitUI
import com.flasshka.activitytrackerdt.ui.habits.DrawerHabitsListWithBottomSheet
import com.flasshka.activitytrackerdt.ui.habits.DrawerSideBar

@Composable
fun MainNavGraph(
    mainVM: MainVM,
    createVM: CreateHabitVM
) {
    val navControllerScreen = rememberNavController()

    NavHost(
        navController = navControllerScreen,
        startDestination = NavScreen.MainScreen.route
    ) {
        composable(NavScreen.MainScreen.route) {
            MainScreenNavigation(
                navControllerScreen = navControllerScreen,
                vm = mainVM
            )
        }

        composable(NavScreen.CreateHabitScreen.route) {
            DrawCreateHabitUI(
                vm = createVM,
                navController = navControllerScreen
            )
        }

        composable(
            route = "${NavScreen.CreateHabitScreen.route}/{itemId}",
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.LongType
                }
            )
        ) {
            val id = it.arguments?.getLong("itemId")
            DrawCreateHabitUI(
                vm = createVM,
                navController = navControllerScreen,
                habitIdToChange = id
            )
        }
    }
}

@Composable
private fun MainScreenNavigation(
    navControllerScreen: NavHostController,
    vm: MainVM
) {
    val navControllerMainScreen = rememberNavController()

    DrawerSideBar(
        vm = vm,
        navController = navControllerMainScreen,
    ) { padding ->
        NavHost(
            navController = navControllerMainScreen,
            startDestination = NavScreen.MainScreen.ListOfHabits.route
        ) {
            composable(NavScreen.MainScreen.ListOfHabits.route) {
                DrawerHabitsListWithBottomSheet(
                    padding = padding,
                    navController = navControllerScreen,
                    vm = vm
                )
            }

            composable(route = NavScreen.MainScreen.InfoAboutApp.route) {
                Info()
            }
        }
    }
}