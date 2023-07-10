package com.igordudka.medita.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.igordudka.medita.R
import com.igordudka.medita.ui.components.DefaultNumTextField
import com.igordudka.medita.ui.components.DefaultScreen
import com.igordudka.medita.ui.components.DefaultTextButton
import com.igordudka.medita.ui.components.DefaultTextField
import com.igordudka.medita.ui.components.Logo
import com.igordudka.medita.ui.theme.interFamily
import com.igordudka.medita.ui.theme.size18
import com.igordudka.medita.ui.theme.size25
import com.igordudka.medita.ui.viewmodels.LoginViewModel
import com.igordudka.medita.ui.viewmodels.ProfileViewModel

@Composable
fun LoginScreen(
    goToMain: () -> Unit,
    changeName: (String) -> Unit,
    changeDailyTarget: (Int) -> Unit,
    launchApp: () -> Unit
) {

    var name by rememberSaveable {
        mutableStateOf("")
    }
    var dailyTarget by rememberSaveable {
        mutableStateOf("")
    }
    var isNameFailure by rememberSaveable {
        mutableStateOf(false)
    }
    var isTargetFailure by rememberSaveable {
        mutableStateOf(false)
    }
    var targetFailureType by rememberSaveable {
        mutableStateOf(0)
    }


    DefaultScreen {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Logo(isBig = false)
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = stringResource(id = R.string.tell_us),
                fontSize = size25,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = interFamily
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LoginText(text = stringResource(id = R.string.enter_your_name))
            DefaultTextField(text = name, onTextChanged = {name = it}, title = stringResource(id = R.string.name), isNameFailure,
            stringResource(id = R.string.name_cannot), {
                })
            LoginText(text = stringResource(id = R.string.enter_your_daily_target))
            DefaultNumTextField(text = dailyTarget, onTextChanged = {dailyTarget = it}, title = stringResource(id = R.string.minutes), isTargetFailure,
            if (targetFailureType == 0) stringResource(id = R.string.target_cannot) else stringResource(
                id = R.string.target_mustbe
            ), {

                })
        }
        DefaultTextButton(text = stringResource(id = R.string.done)) {
            if (dailyTarget.contains(".") || dailyTarget.contains("-")
                || dailyTarget.contains(" ") || dailyTarget.contains(",")){
                isTargetFailure = true
                targetFailureType = 1
            }else{
                if (name == "" || dailyTarget == ""){
                    if (name == ""){
                        isNameFailure = true
                    }
                    if (dailyTarget == ""){
                        isTargetFailure = true
                        targetFailureType = 0
                    }
                }else{
                    changeName(name)
                    changeDailyTarget(dailyTarget.toInt())
                    launchApp()
                    goToMain()
                }
            }
        }
    }
}

@Composable
fun LoginText(
    text: String
) {
    Text(text = text, fontSize = size18, color = Color.White,
    modifier = Modifier.padding(8.dp), fontFamily = interFamily)
}