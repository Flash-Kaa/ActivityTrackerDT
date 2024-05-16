package com.flasshka.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.flasshka.presentation.Info
import com.flasshka.presentation.MainVM
import com.flasshka.presentation.creating.CreateHabitVM
import com.flasshka.presentation.creating.DrawCreateHabitUI
import com.flasshka.presentation.habits.DrawerHabitsList
import com.flasshka.presentation.habits.DrawerSideBar

@Composable
fun MainNavGraph(
    mainVM: MainVM,
    createVM: CreateHabitVM
) {
    val navController = rememberNavController()
    mainVM.initRouter(navController)

    DrawerSideBar(
        vm = mainVM,
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = NavScreen.MainScreen.ListOfHabits.route
        ) {
            composable(NavScreen.MainScreen.ListOfHabits.route) {
                DrawerHabitsList(
                    padding = padding,
                    vm = mainVM
                )
            }

            composable(route = NavScreen.MainScreen.InfoAboutApp.route) {
                Info()
            }

            composable(NavScreen.CreateHabitScreen.route) {
                DrawCreateHabitUI(
                    vm = createVM
                )
            }

            composable(
                route = "${NavScreen.CreateHabitScreen.route}/{itemId}",
                arguments = listOf(
                    navArgument("itemId") {
                        type = NavType.StringType
                    }
                )
            ) {
                DrawCreateHabitUI(
                    vm = createVM,
                    habitIdToChange = it.arguments?.getString("itemId")
                )
            }
        }
    }
}