package com.flasshka.presentation.navigation

import androidx.navigation.NavController
import javax.inject.Inject

class Router @Inject constructor() {
    private lateinit var navController: NavController

    fun init(navController: NavController) {
        this.navController = navController
    }

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