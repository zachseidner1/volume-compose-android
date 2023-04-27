package com.cornellappdev.android.volume.ui.components.general

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.android.volume.R
import com.cornellappdev.android.volume.ui.theme.VolumeOrange
import com.cornellappdev.android.volume.ui.theme.notoserif

@Composable
fun BigReadShimmeringArticle() {
    Column (modifier = Modifier.padding(end = 24.dp)) {
        Box(modifier = Modifier
            .shimmerEffect()
            .requiredSize(180.dp, 180.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier
            .shimmerEffect()
            .requiredSize(180.dp, 20.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier
            .shimmerEffect()
            .requiredSize(60.dp, 20.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier
            .shimmerEffect()
            .requiredSize(100.dp, 20.dp))
    }
}

@Composable
fun ShimmeringArticle(modifier: Modifier = Modifier) {
    Row (modifier = modifier
        .fillMaxWidth()
        .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,) {
        Column {
            Box(modifier = Modifier
                .shimmerEffect()
                .requiredSize(100.dp, 16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier
                .shimmerEffect()
                .requiredSize(200.dp, 16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier
                .shimmerEffect()
                .requiredSize(60.dp, 16.dp))
        }
        Box (modifier = Modifier
            .shimmerEffect()
            .requiredSize(100.dp, 100.dp))
    }
}

@Composable
fun VolumeLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = VolumeOrange)
    }
}

// TODO implement component when internet is down
@Composable
fun VolumeError(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.ic_nowifi), contentDescription = null)
        Text(text = "No Connection", fontFamily = notoserif, modifier = Modifier.padding(top = 32.dp), fontSize = 24.sp)
        Text(text = "Please try again later", fontSize = 17.sp)
    }
}