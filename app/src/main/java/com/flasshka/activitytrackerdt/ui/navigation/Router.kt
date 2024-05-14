package com.flasshka.activitytrackerdt.ui.navigation

import androidx.navigation.NavController

class Router(private val navController: NavController) {
    fun getNavigateToChangeHabit(uid: String): () -> Unit {
        return { navController.navigate("${NavScreen.CreateHabitScreen.route}/${uid}") }
    }

    fun getNavigateToCreateHabit(): () -> Unit {
        return { navController.navigate(NavScreen.CreateHabitScreen.route) }
    }

    fun getNavigateToInfoAction(): () -> Unit {
        return { navController.navigate(NavScreen.MainScreen.InfoAboutApp.route) }
    }

    fun getNavigateToListOfHabits(): () -> Unit {
        return { navController.navigate(NavScreen.MainScreen.ListOfHabits.route) }
    }
}