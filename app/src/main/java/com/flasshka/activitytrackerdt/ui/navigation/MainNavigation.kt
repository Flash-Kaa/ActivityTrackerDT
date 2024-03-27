package com.flasshka.activitytrackerdt.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.flasshka.activitytrackerdt.ui.CreateHabitUI
import com.flasshka.activitytrackerdt.ui.HabitsListWithBottomSheet
import com.flasshka.activitytrackerdt.ui.Info
import com.flasshka.activitytrackerdt.ui.NavigateDrawer
import com.flasshka.activitytrackerdt.viewmodels.MainVM

private var curMainScreen: NavScreen = NavScreen.MainScreen.ListOfHabits
private var curScreen: NavScreen = NavScreen.MainScreen

@Composable
fun MainNavGraph(vm: MainVM) {
    val navControllerScreen = rememberNavController()

    NavHost(
        navController = navControllerScreen,
        startDestination = curScreen.route
    ) {

        composable(NavScreen.MainScreen.route) {
            curScreen = NavScreen.MainScreen
            MainScreenNavigation(
                navControllerScreen = navControllerScreen,
                vm = vm
            )
        }

        composable(NavScreen.CreateHabitScreen.route) {
            curScreen = NavScreen.CreateHabitScreen

            CreateHabitUI(vm, navControllerScreen, null)
        }

        composable(
            route = "${NavScreen.CreateHabitScreen.route}/{item}",
            arguments = listOf(navArgument("item") {
                type = NavType.StringType
            })
        ) {
            curScreen = NavScreen.CreateHabitScreen

            val id = it.arguments?.getString("item")?.toLong()

            CreateHabitUI(vm, navControllerScreen, id)
        }
    }
}

@Composable
private fun MainScreenNavigation(
    navControllerScreen: NavHostController,
    vm: MainVM
) {
    val navControllerMainScreen = rememberNavController()

    NavigateDrawer(navControllerMainScreen) { padding ->
        NavHost(
            navController = navControllerMainScreen,
            startDestination = curMainScreen.route
        ) {
            composable(NavScreen.MainScreen.ListOfHabits.route) {
                curMainScreen = NavScreen.MainScreen.ListOfHabits

                HabitsListWithBottomSheet(
                    padding = padding,
                    navControllerScreen = navControllerScreen,
                    vm = vm
                )
            }

            composable(route = NavScreen.MainScreen.InfoAboutApp.route) {
                curMainScreen = NavScreen.MainScreen.InfoAboutApp
                Info()
            }
        }
    }
}