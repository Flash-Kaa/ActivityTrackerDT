package com.flasshka.activitytrackerdt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.flasshka.activitytrackerdt.ui.theme.ActivityTrackerDTTheme
import com.flasshka.activitytrackerdt.viewmodels.CreateHabitVM

class CreateHabitActivity : ComponentActivity() {
    private val vm: CreateHabitVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.loadFromIntent(intent, this)

        setContent {
            ActivityTrackerDTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    vm.Drawer(this)
                }
            }
        }
    }
}