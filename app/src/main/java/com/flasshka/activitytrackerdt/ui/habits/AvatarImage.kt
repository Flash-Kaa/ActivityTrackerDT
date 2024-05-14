package com.flasshka.activitytrackerdt.ui.habits

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.flasshka.activitytrackerdt.R


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Avatar() {
    GlideImage(
        model = stringResource(R.string.avatar_uri),
        contentDescription = "",
        loading = placeholder(R.drawable.avatar),
        failure = placeholder(R.drawable.avatar),
        alignment = Alignment.TopCenter,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(15.dp)
            .size(100.dp)
            .clip(CircleShape)
            .border(2.dp, Color.Gray, CircleShape)
    )
}