package com.igordudka.medita.ui.screens

import android.window.SplashScreen
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.igordudka.medita.R
import com.igordudka.medita.ui.theme.interFamily
import com.igordudka.medita.ui.theme.size42
import com.igordudka.medita.ui.theme.size50
import com.igordudka.medita.ui.viewmodels.LoginViewModel
import com.igordudka.medita.ui.viewmodels.ProfileViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    goNext: () -> Unit,
) {
    var isStarted by rememberSaveable {
        mutableStateOf(false)
    }

    val padding by animateDpAsState(
        targetValue = if (isStarted) 90.dp else 200.dp,
        animationSpec = tween(2500)
    )
    val splashWidth by animateDpAsState(
        targetValue = if (isStarted) 105.dp else 150.dp,
        animationSpec = tween(2500)
    )
    val splashHeight by animateDpAsState(
        targetValue = if (isStarted) 158.dp else 200.dp,
        animationSpec = tween(2500)
    )
    val textAlpha by animateFloatAsState(
        targetValue = if (isStarted) 1f else 0f,
        animationSpec = tween(2500)
    )

    isStarted = true

    LaunchedEffect(key1 = true){
        delay(3000)
        goNext()
    }


    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       Box(contentAlignment = Alignment.Center){
           Image(painter = painterResource(id = R.drawable.elipse), contentDescription = null,
           Modifier.size(110.dp))
           Image(painter = painterResource(id = R.drawable.splash1), contentDescription = null,
               Modifier
                   .padding(end = padding)
                   .width(splashWidth)
                   .height(splashHeight))
           Image(painter = painterResource(id = R.drawable.splash2), contentDescription = null,
               Modifier
                   .padding(start = padding)
                   .width(splashWidth)
                   .height(splashHeight))
       }
        Text(
            text = "medita",
            color = Color.White,
            fontFamily = interFamily,
            fontSize = size42,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.alpha(textAlpha)
        )
    }
}