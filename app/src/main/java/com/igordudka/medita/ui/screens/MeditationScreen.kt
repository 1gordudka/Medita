package com.igordudka.medita.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.igordudka.medita.R
import com.igordudka.medita.ui.components.DefaultDialog
import com.igordudka.medita.ui.components.DefaultTextButton
import com.igordudka.medita.ui.theme.allBacks
import com.igordudka.medita.ui.theme.interFamily
import com.igordudka.medita.ui.theme.mainBack
import com.igordudka.medita.ui.theme.size18
import com.igordudka.medita.ui.theme.size23
import com.igordudka.medita.ui.theme.size25
import com.igordudka.medita.ui.theme.size42
import com.igordudka.medita.ui.theme.size69
import com.igordudka.medita.ui.viewmodels.MeditationViewModel
import com.igordudka.medita.ui.viewmodels.ProfileViewModel
import com.igordudka.medita.ui.viewmodels.allAudio
import kotlinx.coroutines.delay

val logoWidths = listOf(256.dp, 280.dp, 256.dp, 320.dp)
val logoHeights = listOf(171.dp, 195.dp, 171.dp, 215.dp)

@Composable
fun MeditationScreen(
    goBack: () -> Unit,
    time: Int,
    music: Int,
    changeTodayMinutes: () -> Unit,
    stopMusic: () -> Unit
) {

    var currentColor by rememberSaveable {
        mutableStateOf(0)
    }
    val backColor by animateColorAsState(targetValue = allBacks[currentColor % 5],
    animationSpec = tween(2000)
    )
    var currentSize by rememberSaveable {
        mutableStateOf(0)
    }
    val logoWidth by animateDpAsState(targetValue = logoWidths[currentSize % 4])
    val logoHeight by animateDpAsState(targetValue = logoHeights[currentSize % 4])
    var isStopDialogShown by rememberSaveable {
        mutableStateOf(false)
    }
    var currentSeconds by rememberSaveable {
        mutableStateOf(time * 60)
    }
    var secondsCompleted by rememberSaveable {
        mutableStateOf(1)
    }
    var starterScreen by rememberSaveable {
        mutableStateOf(0)
    }

    LaunchedEffect(key1 = Unit){
        delay(4000)
        starterScreen++
        delay(4000)
        starterScreen++
        delay(4000)
        starterScreen++
        delay(4000)
        starterScreen++
    }

    if (starterScreen == 0){
        AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
            StarterMeditationScreen(text = stringResource(id = R.string.keep_calm))
        }
    }else if(starterScreen == 1){
        AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()){
            StarterMeditationScreen(text = stringResource(id = R.string.disable_notifications))
        }
    }else if(starterScreen == 2){
        AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut(tween(2000))){
            StarterMeditationScreen(text = stringResource(id = R.string.close_eyes))
        }
    }else{
        if (currentSeconds == 0){
            DefaultDialog(
                content = {
                    Text(text = stringResource(id = R.string.meditation_over),
                        color = Color.White, fontSize = size23, fontFamily = interFamily)
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(text = stringResource(id = R.string.congrats),
                        color = Color.White, fontSize = size23, fontFamily = interFamily)},
                title = { /*TODO*/ },
                confirmButton = { DefaultTextButton(text = stringResource(id = R.string.go_home)) {
                    goBack()
                } }) {}
        }
        if (isStopDialogShown){
            DefaultDialog(content = {
                Text(
                    text = stringResource(id = R.string.do_you_want),
                    color = Color.White,
                    fontFamily = interFamily,
                    fontSize = size25
                )
            }, title = { }, confirmButton = { DefaultTextButton(text = stringResource(id = R.string.yes)) {
                if (music != allAudio.size){
                    stopMusic()
                }
                goBack()
                isStopDialogShown = false
            }}) {
                DefaultTextButton(text = stringResource(id = R.string.no)) {
                    isStopDialogShown = false
                }
            }
        }
        var isVisible by rememberSaveable {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = Unit){
            isVisible = true
            while (currentSeconds > 0){
                delay(1000)
                currentSeconds--
                secondsCompleted++
                if (secondsCompleted % 60 == 0){
                    changeTodayMinutes()
                }
            }
            if (music != allAudio.size){
                stopMusic()
            }
        }

        LaunchedEffect(key1 = Unit){
            while (true){
                delay(1000)
                currentSize++
            }
        }

        LaunchedEffect(key1 = Unit){
            while (true){
                delay(2000)
                currentColor++
            }
        }


        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(tween(2000)),
            exit = ExitTransition.None
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                backColor,
                                Color.Black.copy(alpha = 0f)
                            )
                        )
                    )
                    .systemBarsPadding(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = stringResource(id = R.string.meditation),
                    color = Color.White, fontFamily = interFamily, fontSize = size18,
                    textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
                Box(modifier = Modifier.size(320.dp, 215.dp),
                    contentAlignment = Alignment.Center){
                    Image(painter = painterResource(id = R.drawable.big_logo), contentDescription = null,
                        Modifier
                            .width(logoWidth)
                            .height(logoHeight))
                }
                Text(
                    text = "$currentSeconds",
                    color = Color.White,
                    fontSize = size42,
                    fontFamily = interFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                DefaultTextButton(text = stringResource(id = R.string.stop)) {
                    isStopDialogShown = true
                }
            }
        }
    }
}

@Composable
fun StarterMeditationScreen(
    text: String
) {

    var current by remember {
        mutableStateOf(false)
    }
    val textAlpha by animateFloatAsState(targetValue = if (current) 1f else 0f, animationSpec =
    tween(4000)
    )

    LaunchedEffect(key1 = Unit){
        current = true
        delay(2000)
        current = false
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = size42,
            color = Color.White,
            fontFamily = interFamily,
            modifier = Modifier.alpha(textAlpha),
            textAlign = TextAlign.Center
        )
    }
}