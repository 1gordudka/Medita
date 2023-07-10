package com.igordudka.medita.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.igordudka.medita.ui.theme.size42
import com.igordudka.medita.ui.theme.size50
import com.igordudka.medita.ui.viewmodels.Meditation
import com.igordudka.medita.ui.viewmodels.Music
import com.igordudka.medita.ui.viewmodels.ProfileViewModel
import com.igordudka.medita.ui.viewmodels.allMeditations
import com.igordudka.medita.ui.viewmodels.allMusic
import com.igordudka.medita.ui.viewmodels.allSounds
import kotlinx.coroutines.delay

val minutesList = listOf(5, 10, 15, 30, 45, 60)

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("PermissionLaunchedDuringComposition", "RestrictedApi", "VisibleForTests")
@Composable
fun MainScreen(
    goToProfile: () -> Unit,
    time: Int,
    music: Int,
    sound: Int,
    meditation: Int,
    isFirstTime: Boolean,
    doFirstTimeLaunch: () -> Unit,
    name: String,
    minutesToday: Int,
    dailyTarget: Int,
    changeSoundStatus: (Boolean) -> Unit,
    changeMusicStatus: (Boolean) -> Unit,
    changeMeditationStatus: (Boolean) -> Unit,
    isSound: Boolean,
    isMusic: Boolean,
    isMeditation: Boolean,
    startMeditation: () -> Unit,
    chooseTime: (Int) -> Unit,
    chooseSound: (Int) -> Unit,
    chooseMeditation: (Int) -> Unit,
    chooseMusic: (Int) -> Unit,
    startPreview: (Int) -> Unit,
    stopPreview: () -> Unit,
    previewRaw: Int,
    isPlaying: Boolean
) {


    var currentPart by rememberSaveable {
        mutableStateOf(0)
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
    horizontalAlignment = Alignment.CenterHorizontally) {
        MainTopBar(name = name) {
            goToProfile()
        }
        Row {
            StatsCard(amount = minutesToday, description = "" +
                    stringResource(id = R.string.min_today), modifier = Modifier.weight(1f)) {
            }
            if (minutesToday < dailyTarget){
                StatsCard(amount = dailyTarget - minutesToday, description = stringResource(id = R.string.to_daily), modifier = Modifier.weight(1f)) {}
            }else{
                CongratsCard(modifier = Modifier.weight(1f))
            }
        }
        AnimatedVisibility(visible = currentPart == 0,
            enter = expandHorizontally(expandFrom = Alignment.End),
            exit = ExitTransition.None
        ) {
            Column(
                Modifier
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                MinutesBlock(chosenItem = time, choose = { chooseTime(it) })
                DefaultTextButton(text = stringResource(id = R.string.next)) {
                    if (time != -1){
                        currentPart++
                    }else{
                        Toast.makeText(context, context.getText(R.string.time_choose), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        AnimatedVisibility(visible = currentPart == 1, enter = expandHorizontally(expandFrom = Alignment.End),
            exit = ExitTransition.None
        ) {
            Column(
                Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                MeditationBlock(
                    isMeditation = isMeditation,
                    changeMeditation = { changeMeditationStatus(it) },
                    chosenItem = meditation,
                    choose = {
                    if (meditation == it && isPlaying){
                        stopPreview()
                    }else{
                        chooseMeditation(it)
                        startPreview(allMeditations[it].raw)
                    }
                    },
                    goBack = {currentPart--},
                    isPlaying = isPlaying,
                    previewRaw = previewRaw
                )
                DefaultTextButton(text = stringResource(id = R.string.next)) {
                    stopPreview()
                    currentPart++
                }
            }
        }
        AnimatedVisibility(visible = currentPart == 2, enter = expandHorizontally(expandFrom = Alignment.End),
        exit = ExitTransition.None) {
            LazyColumn(
                Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item{
                    SoundBlock(chosenItem = sound, choose = {
                        if (sound == it && isPlaying){
                            stopPreview()
                        }else{
                            chooseSound(it)
                            startPreview(allSounds[it].raw)
                        } },
                        changeSound = {changeSoundStatus(it)
                                      changeMusicStatus(!it)}, isSound = isSound, goBack = {currentPart--},
                    isPlaying = isPlaying, previewRaw = previewRaw)
                    MusicBlock(
                        isMusic = isMusic,
                        changeMusic = { changeMusicStatus(it)
                                      changeSoundStatus(!it)},
                        chosenItem = music,
                        choose = {

                            if (music == it && isPlaying){
                                stopPreview()
                            }else{
                                chooseMusic(it)
                                startPreview(allMusic[it].raw)
                            }
                        },
                        isPlaying = isPlaying,
                        previewRaw = previewRaw
                    )
                    DefaultTextButton(text = stringResource(id = R.string.start)) {
                        stopPreview()
                        startMeditation()
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        var isAlpha by remember {
            mutableStateOf(false)
        }
        val meditaAlpha by animateFloatAsState(targetValue = if (isAlpha) 1f else 0f,
        animationSpec = tween(3000)
        )
        LaunchedEffect(key1 = Unit){
            while (true){
                delay(3000)
                isAlpha = !isAlpha
            }
        }
        Text(text = "medita", color = Color.White.copy(alpha = 0.7f), fontSize = size23,
        fontFamily = interFamily, modifier = Modifier
                .padding(16.dp)
                .alpha(meditaAlpha))
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
fun SoundCard(
    onClick: () -> Unit,
    pic: Int,
    isChosen: Boolean,
    isPlaying: Boolean
) {

    val sizesList = listOf(150.dp, 130.dp)
    var playingState by remember {
        mutableStateOf(0)
    }

    if (isPlaying){
        LaunchedEffect(key1 = Unit){
            while (true){
                delay(500)
                playingState++
            }
        }
    }

    val width by animateDpAsState(targetValue = if (isChosen && !isPlaying) 150.dp else if(isPlaying
        && isChosen) sizesList[playingState % 2]
    else 120.dp)
    val height by animateDpAsState(targetValue = if (isChosen && !isPlaying) 150.dp else if(isPlaying
        && isChosen) sizesList[playingState % 2]
    else 120.dp)
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
fun SoundBlock(isSound: Boolean, changeSound: (Boolean) -> Unit, chosenItem: Int, choose: (Int) -> Unit,
goBack: () -> Unit, isPlaying: Boolean, previewRaw: Int) {

    Column(horizontalAlignment = Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = goBack, Modifier.padding(start = 16.dp)) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Text(text = stringResource(id = R.string.sounds), fontSize = size23, fontFamily = interFamily, color = Color.White,
                modifier = Modifier.padding(12.dp))
            Switch(checked = isSound, onCheckedChange = { changeSound(it) },
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color.White,
                checkedThumbColor = Color.White,
                uncheckedTrackColor = Color.Transparent,
                checkedTrackColor = Color.Transparent,
                uncheckedBorderColor = Color.White,
                checkedBorderColor = Color.White
            ))
        }
        AnimatedVisibility(visible = isSound, enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
        ) {
            LazyRow{
                items(allSounds){
                    SoundCard(onClick = { choose(allSounds.indexOf(it)) }, pic = it.pic, isChosen = chosenItem ==
                            allSounds.indexOf(it), isPlaying = isPlaying && previewRaw == it.raw)
                }
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

    val width by animateDpAsState(targetValue = if (isChosen) 100.dp else 100.dp)
    val height by animateDpAsState(targetValue = if (isChosen) 120.dp else 100.dp)
    val color by animateColorAsState(targetValue = if (isChosen) Color.White else Color.Gray)

    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .width(120.dp)
        .height(140.dp)) {
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
            modifier = Modifier.padding(16.dp))
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

@Composable
fun MeditationBlock(
    isMeditation: Boolean, changeMeditation: (Boolean) -> Unit, chosenItem: Int, choose: (Int) -> Unit,
    goBack: () -> Unit, previewRaw: Int, isPlaying: Boolean
) {

    Column(horizontalAlignment = Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = goBack, Modifier.padding(start = 16.dp)) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Text(text = stringResource(id = R.string.meditation), fontSize = size23, fontFamily = interFamily, color = Color.White,
                modifier = Modifier.padding(12.dp))
            Switch(checked = isMeditation, onCheckedChange = { changeMeditation(it) },
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color.White,
                    checkedThumbColor = Color.White,
                    uncheckedTrackColor = Color.Transparent,
                    checkedTrackColor = Color.Transparent,
                    uncheckedBorderColor = Color.White,
                    checkedBorderColor = Color.White
                ))
        }
        AnimatedVisibility(visible = isMeditation, enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            LazyRow{
                items(allMeditations){
                    SoundCard(onClick = { choose(allMeditations.indexOf(it)) }, pic = it.pic, isChosen = chosenItem ==
                            allMeditations.indexOf(it), isPlaying = isPlaying && previewRaw == it.raw)
                }
            }
        }
    }
}

@Composable
fun MusicBlock(isMusic: Boolean, changeMusic: (Boolean) -> Unit, chosenItem: Int, choose: (Int) -> Unit,
isPlaying: Boolean, previewRaw: Int) {

    Column(horizontalAlignment = Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.music), fontSize = size23, fontFamily = interFamily, color = Color.White,
                modifier = Modifier.padding(16.dp))
            Switch(checked = isMusic, onCheckedChange = { changeMusic(it) },
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color.White,
                    checkedThumbColor = Color.White,
                    uncheckedTrackColor = Color.Transparent,
                    checkedTrackColor = Color.Transparent,
                    uncheckedBorderColor = Color.White,
                    checkedBorderColor = Color.White
                ))
        }
        AnimatedVisibility(visible = isMusic, enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            LazyRow{
                items(allMusic){
                    SoundCard(onClick = { choose(allMusic.indexOf(it)) }, pic = it.pic, isChosen = chosenItem ==
                            allMusic.indexOf(it), isPlaying = isPlaying && previewRaw == it.raw)
                }
            }
        }
    }
}