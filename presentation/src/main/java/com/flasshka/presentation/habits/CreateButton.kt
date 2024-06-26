package com.flasshka.presentation.habits

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.flasshka.presentation.R
import com.flasshka.presentation.TestTags

@Composable
fun CreateButton(
    getAction: (HabitListActionType) -> (() -> Unit)
) {
    Box(contentAlignment = Alignment.BottomCenter) {
        SmallFloatingActionButton(
            modifier = Modifier
                .testTag(TestTags.CREATE_BUTTON.toString())
                .size(80.dp),
            shape = RoundedCornerShape(40.dp),
            onClick = getAction(HabitListActionType.NavigateToCreateHabit)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.baseline_add_120),
                contentDescription = "add_button"
            )
        }
    }
}