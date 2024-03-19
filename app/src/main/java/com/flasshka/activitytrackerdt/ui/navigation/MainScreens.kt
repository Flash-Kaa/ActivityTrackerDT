package com.flasshka.activitytrackerdt.ui.navigation


sealed class MainScreens(val route: String) {
    object InfoAboutApp : MainScreens("info_about_application")
    object ListOfHabits : MainScreens("list_of_habits")
}