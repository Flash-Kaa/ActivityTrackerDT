package com.flasshka.activitytrackerdt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.flasshka.activitytrackerdt.ui.theme.ActivityTrackerDTTheme
import com.flasshka.activitytrackerdt.viewmodels.MainVM

class MainActivity : ComponentActivity() {
    private val vm: MainVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ActivityTrackerDTTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    vm.DrawerList()
                }
            }
        }
    }
}