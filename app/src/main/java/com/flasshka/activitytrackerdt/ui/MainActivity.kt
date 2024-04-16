package com.flasshka.activitytrackerdt.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.flasshka.activitytrackerdt.ui.creating.CreateHabitVM
import com.flasshka.activitytrackerdt.ui.creating.FactoryForCreateVM
import com.flasshka.activitytrackerdt.ui.navigation.MainNavGraph
import com.flasshka.activitytrackerdt.ui.theme.ActivityTrackerDTTheme

class MainActivity : ComponentActivity() {
    private val mainVM: MainVM by viewModels()
    private val createVM: CreateHabitVM by viewModels { FactoryForCreateVM(mainVM) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainVM.initRepository(applicationContext)
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