package com.flasshka.activitytrackerdt.ui.navigation

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.flasshka.activitytrackerdt.ui.HabitsList
import com.flasshka.activitytrackerdt.ui.Info
import com.flasshka.activitytrackerdt.ui.NavigateDrawer
import com.flasshka.activitytrackerdt.viewmodels.MainVM

private var cur: MainScreens = MainScreens.ListOfHabits

@Composable
fun MainNavGraph(
    navController: NavHostController,
    activity: Activity,
    vm: MainVM
) {
    NavHost(
        navController = navController,
        startDestination = cur.route
    ) {
        composable(MainScreens.ListOfHabits.route) {
            cur = MainScreens.ListOfHabits
            Log.i("my_log", "createdNavDrawer")

            NavigateDrawer(navController) {
                HabitsList(
                    padding = it,
                    activity = activity,
                    vm = vm
                )
            }
        }

        composable(route = MainScreens.InfoAboutApp.route) {
            cur = MainScreens.InfoAboutApp
            NavigateDrawer(navController) {
                Info()
            }
        }
    }
}