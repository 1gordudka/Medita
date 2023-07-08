package com.igordudka.medita.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.igordudka.medita.R
import com.igordudka.medita.ui.components.DefaultTextButton
import com.igordudka.medita.ui.components.StatsCard
import com.igordudka.medita.ui.theme.cardColor
import com.igordudka.medita.ui.theme.interFamily
import com.igordudka.medita.ui.theme.mainBack
import com.igordudka.medita.ui.theme.size18
import com.igordudka.medita.ui.theme.size23
import com.igordudka.medita.ui.theme.size35
import com.igordudka.medita.ui.theme.size50
import com.igordudka.medita.ui.viewmodels.LoginViewModel
import com.igordudka.medita.ui.viewmodels.MeditationViewModel
import com.igordudka.medita.ui.viewmodels.NotificationViewModel
import com.igordudka.medita.ui.viewmodels.ProfileViewModel
import com.igordudka.medita.ui.viewmodels.allAudio
import com.igordudka.medita.utils.notifications.RemindersManager
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.Duration
import java.util.concurrent.TimeUnit

val musicList = listOf(R.drawable.forest, R.drawable.fire, R.drawable.waterfall, R.drawable.rain, R.drawable.space,
R.drawable.silence)
val minutesList = listOf(5, 10, 15, 30, 45, 60)

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("PermissionLaunchedDuringComposition", "RestrictedApi", "VisibleForTests")
@Composable
fun MainScreen(
    goToProfile: () -> Unit,
    goToMeditation: () -> Unit,
    time: Int,
    music: Int,
    isFirstTime: Boolean,
    doFirstTimeLaunch: () -> Unit,
    name: String,
    minutesToday: Int,
    dailyTarget: Int,
    startMeditation: (Int, Int) -> Unit
) {

    var chosenTime by rememberSaveable {
        mutableStateOf(time)
    }
    var chosenMusic by rememberSaveable {
        mutableStateOf(music)
    }

    val context = LocalContext.current


    if (isFirstTime){
        doFirstTimeLaunch()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        mainBack,
                        Color.Black.copy(alpha = 0f)
                    )
                )
            )
            .systemBarsPadding(),
    verticalArrangement = Arrangement.SpaceBetween,
    horizontalAlignment = Alignment.CenterHorizontally) {
        MainTopBar(name = name) {
            goToProfile()
        }
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally){
            item {
                Row {
                    StatsCard(amount = minutesToday, description = "" +
                            stringResource(id = R.string.min_today), modifier = Modifier.weight(1f)) {}
                    if (minutesToday < dailyTarget){
                        StatsCard(amount = dailyTarget - minutesToday, description = stringResource(id = R.string.to_daily), modifier = Modifier.weight(1f)) {}
                    }else{
                        CongratsCard(modifier = Modifier.weight(1f))
                    }
                }
                MusicBlock(chosenItem = chosenMusic, choose = { chosenMusic = it })
                MinutesBlock(chosenItem = chosenTime, choose = { chosenTime = it })
            }
        }
        DefaultTextButton(text = stringResource(id = R.string.start)) {
            if (chosenMusic != -1 && chosenTime != -1){
                startMeditation(chosenTime, chosenMusic)
            }else{
                Toast.makeText(context, context.getText(R.string.warning), Toast.LENGTH_SHORT).show()
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun MainTopBar(
    name: String,
    goToProfile: () -> Unit
) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
        Text(text = stringResource(id = R.string.hello) + "\n$name", fontSize = size35, color = Color.White,
        fontWeight = FontWeight.Medium, fontFamily = interFamily
        )
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = goToProfile) {
            Image(painter = painterResource(id = R.drawable.profile_icn), contentDescription = null,
            Modifier.size(40.dp))
        }
    }
}

@Composable
fun MusicCard(
    onClick: () -> Unit,
    pic: Int,
    isChosen: Boolean
) {

    val width by animateDpAsState(targetValue = if (isChosen) 150.dp else 120.dp)
    val height by animateDpAsState(targetValue = if (isChosen) 150.dp else 120.dp)
    val alpha by animateFloatAsState(targetValue = if (isChosen) 1f else 0.7f)

    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .width(165.dp)
        .height(165.dp)) {
        Image(painter = painterResource(id = pic), contentDescription = null,
            Modifier
                .width(width)
                .height(height)
                .clip(RoundedCornerShape(27.dp))
                .clickable {
                    onClick()
                }
                .alpha(alpha), contentScale = ContentScale.Crop)
    }
}

@Composable
fun MusicBlock(chosenItem: Int, choose: (Int) -> Unit) {

    Column {
        Text(text = stringResource(id = R.string.choose_music), fontSize = size23, fontFamily = interFamily, color = Color.White,
        modifier = Modifier.padding(12.dp))
        LazyRow{
            items(musicList){
                MusicCard(onClick = { choose(musicList.indexOf(it)) }, pic = it, isChosen = chosenItem ==
                musicList.indexOf(it))
            }
        }
    }
}

@Composable
fun MinutesCard(
    onClick: () -> Unit,
    minutes: Int,
    isChosen: Boolean
) {

    val width by animateDpAsState(targetValue = if (isChosen) 120.dp else 100.dp)
    val height by animateDpAsState(targetValue = if (isChosen) 100.dp else 80.dp)
    val color by animateColorAsState(targetValue = if (isChosen) Color.White else Color.Gray)

    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .width(130.dp)
        .height(110.dp)) {
        Row(
            Modifier
                .clip(RoundedCornerShape(27.dp))
                .background(Color(0xFF000000).copy(alpha = 0.3f))
                .width(width)
                .height(height)
                .clickable { onClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$minutes",
                fontSize = size50,
                fontFamily = interFamily,
                color = color
            )
        }
    }
}

@Composable
fun MinutesBlock(chosenItem: Int, choose: (Int) -> Unit) {

    Column {
        Text(text = stringResource(id = R.string.choose_time), fontSize = size23, fontFamily = interFamily, color = Color.White,
            modifier = Modifier.padding(12.dp))
        LazyRow{
            items(minutesList){
                MinutesCard(onClick = { choose(it) }, minutes = it, isChosen = it == chosenItem)
            }
        }
    }
}

@Composable
fun CongratsCard(
    modifier: Modifier
) {

    Card(modifier = modifier
        .padding(12.dp)
        .sizeIn(minHeight = 100.dp, minWidth = 100.dp)
        .clip(RoundedCornerShape(26.dp)), shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)) {
        Column(Modifier.padding(16.dp)
        ) {
            Text(text = stringResource(id = R.string.congrats), fontSize = size23, fontFamily = interFamily,
            fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(7.dp))
            Text(text = stringResource(id = R.string.you_reached), color = Color.White, fontFamily = interFamily,
            fontSize = size18)
        }
    }
}