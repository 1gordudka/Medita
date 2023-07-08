package com.igordudka.medita.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.igordudka.medita.R
import com.igordudka.medita.ui.components.DefaultDialog
import com.igordudka.medita.ui.components.DefaultNumTextField
import com.igordudka.medita.ui.components.DefaultScreen
import com.igordudka.medita.ui.components.DefaultTextButton
import com.igordudka.medita.ui.components.DefaultTextField
import com.igordudka.medita.ui.components.Logo
import com.igordudka.medita.ui.components.StatsCard
import com.igordudka.medita.ui.theme.interFamily
import com.igordudka.medita.ui.theme.size18
import com.igordudka.medita.ui.theme.size23
import com.igordudka.medita.ui.theme.size35
import com.igordudka.medita.ui.viewmodels.NotificationViewModel
import com.igordudka.medita.ui.viewmodels.ProfileViewModel
import com.igordudka.medita.utils.notifications.RemindersManager

@Composable
fun ProfileScreen(
    goBack: () -> Unit,
    changeDailyTarget: (Int) -> Unit,
    time: Int,
    notifications: Boolean,
    dailyTarget: Int,
    name: String,
    averageLastWeek: Int,
    average: Int,
    changeNotifications: (Boolean) -> Unit,
    changeNotificationsTime: (Int) -> Unit
) {

    var isDailyTargetChangeDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var newDailyTarget by rememberSaveable {
        mutableStateOf("")
    }
    val context = LocalContext.current

    if (isDailyTargetChangeDialog){
        DefaultDialog(content = { DefaultNumTextField(text = newDailyTarget, onTextChanged = {newDailyTarget = it}, title = stringResource(
            id = R.string.minutes
        ), isFailure = false, "", {}) }, title = {
                      Text(text = stringResource(id = R.string.change_daily_target), color = Color.White, fontSize = size23,
                      fontFamily = interFamily)
        }, confirmButton = { DefaultTextButton(text = stringResource(id = R.string.done)) {
            changeDailyTarget(newDailyTarget.toInt())
            isDailyTargetChangeDialog = false
        } }) {
            DefaultTextButton(text = stringResource(id = R.string.disable)) {
                isDailyTargetChangeDialog = false
            }
        }
    }


    DefaultScreen {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)) {
            IconButton(onClick = { goBack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null,
                Modifier.size(40.dp), tint = Color.White)
            }
        }
        Text(text = name, color = Color.White, fontFamily = interFamily,
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = size35, fontWeight = FontWeight.Medium)
        Row(Modifier.fillMaxWidth()) {
            StatsCard(amount = dailyTarget, description = stringResource(id = R.string.weekly_target), modifier = Modifier.weight(
                1f
            )) {isDailyTargetChangeDialog = true}
            StatsCard(amount = average, description = stringResource(id = R.string.average), modifier = Modifier.weight(1f)) {}
        }
        Row {
            StatsCard(amount = averageLastWeek, description = stringResource(id = R.string.average_last_week), modifier = Modifier.weight(1f)) {}
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround) {
            Text(
                text = stringResource(id = R.string.daily_notifications),
                color = Color.White,
                fontSize = size18,
                fontFamily = interFamily
            )
            Switch(checked = notifications, onCheckedChange = {
                changeNotifications(it)
                    if (it == false){
                        RemindersManager(context = context).stopReminder()
                    }else{
                        RemindersManager(context).startReminder("$time:00")
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedBorderColor = Color.White,
                    uncheckedBorderColor = Color.White,
                    uncheckedThumbColor = Color.White,
                    checkedTrackColor = Color.Black,
                    uncheckedTrackColor = Color.Black
                ))
        }
        AnimatedVisibility(
            visible = notifications,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ){
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(id = R.string.notification_time),
                    color = Color.White,
                    fontSize = size18,
                    fontFamily = interFamily
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = {
                        if (time == 0){
                            changeNotificationsTime(23)
                                }else{
                                    changeNotificationsTime(time - 1)
                                }
                            }) {
                                Text(
                                    text = "-",
                                    color = Color.White,
                                    fontFamily = interFamily,
                                    fontSize = size35,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "$time:00",
                                color = Color.White,
                                fontSize = size18,
                                fontFamily = interFamily)
                            Spacer(modifier = Modifier.width(5.dp))
                            TextButton(onClick = {
                                if (time == 23){
                                    changeNotificationsTime(0)
                                }else{
                                    changeNotificationsTime(time + 1)
                                }
                            }) {
                                Text(
                                    text = "+",
                                    color = Color.White,
                                    fontFamily = interFamily,
                                    fontSize = size35,
                                    fontWeight = FontWeight.Bold
                                )
                    }
                }
            }
        }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.weight(1f))
            Logo(isBig = false)
        }
    }
}