package com.flasshka.presentation.navigation


sealed class NavScreen(val route: String) {
    object MainScreen : NavScreen("main_screen") {
        object InfoAboutApp : NavScreen("info_about_application")
        object ListOfHabits : NavScreen("list_of_habits")
    }

    object CreateHabitScreen : NavScreen("create_habit")
}