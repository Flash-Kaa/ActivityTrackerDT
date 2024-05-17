package com.flasshka.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.flasshka.presentation.creating.CreateHabitVM
import com.flasshka.presentation.creating.FactoryForCreateVM
import com.flasshka.presentation.navigation.MainNavGraph
import com.flasshka.presentation.ui.theme.ActivityTrackerDTTheme

class MainActivity : ComponentActivity() {
    private val mainVM: MainVM by viewModels()
    private val createVM: CreateHabitVM by viewModels { FactoryForCreateVM(mainVM) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainVM.initDb(applicationContext)
        mainVM.loadListOfHabits()
        mainVM.initToast(this)


        setContent {
            ActivityTrackerDTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavGraph(
                        mainVM = mainVM,
                        createVM = createVM
                    )
                }
            }
        }
    }
}