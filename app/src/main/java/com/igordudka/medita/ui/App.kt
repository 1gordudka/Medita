package com.igordudka.medita.ui

import android.Manifest
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.igordudka.medita.ui.components.DefaultScreen
import com.igordudka.medita.ui.screens.LoginScreen
import com.igordudka.medita.ui.screens.MainScreen
import com.igordudka.medita.ui.screens.MeditationScreen
import com.igordudka.medita.ui.screens.ProfileScreen
import com.igordudka.medita.ui.screens.SplashScreen
import com.igordudka.medita.ui.viewmodels.LoginViewModel
import com.igordudka.medita.ui.viewmodels.MeditationViewModel
import com.igordudka.medita.ui.viewmodels.NotificationViewModel
import com.igordudka.medita.ui.viewmodels.ProfileViewModel
import com.igordudka.medita.ui.viewmodels.StatisticsViewModel
import com.igordudka.medita.utils.notifications.RemindersManager
import com.igordudka.medita.utils.statistics.DaysManager
import com.igordudka.medita.utils.statistics.WeeksManager

val splash = 0
val login = 1
val main = 2
val profile = 3
val meditation = 4

@RequiresApi(VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun App(
    loginViewModel: LoginViewModel = hiltViewModel(),
    meditationViewModel: MeditationViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    statisticsViewModel: StatisticsViewModel = hiltViewModel()
) {

    var currentScreen by rememberSaveable {
        mutableStateOf(splash)
    }
    val context = LocalContext.current
    val notificationPermissionState =
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS
        )

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
    ) {
        AnimatedVisibility(visible = currentScreen == splash,
        enter = fadeIn(), exit = ExitTransition.None
        ) {
            SplashScreen(goNext = {
                if (loginViewModel.isFirst.value == false){
                    currentScreen = main
                }else if(loginViewModel.isFirst.value == true){
                    currentScreen = login
                }
            })
        }
        loginViewModel.isFirst.collectAsState().value?.let { isFirst->
            notificationViewModel.notificationTime.collectAsState().value?.let { notificationTime->
                profileViewModel.todayMinutes.collectAsState().value?.let { todayMinutes->
                    profileViewModel.name.collectAsState().value?.let{name->
                        profileViewModel.dailyTarget.collectAsState().value?.let{dailyTarget->
                            profileViewModel.notifications.collectAsState().value?.let { notifications->
                                statisticsViewModel.days.collectAsState().value?.let { days->
                                    statisticsViewModel.minutes.collectAsState().value?.let { minutes->
                                        statisticsViewModel.lastWeekStats.collectAsState().value?.let { lastWeekStats->
                                            AnimatedVisibility(
                                                visible = currentScreen == login,
                                                enter = fadeIn(),
                                                exit = ExitTransition.None
                                            ) {
                                                LoginScreen(
                                                    goToMain = { currentScreen = main },
                                                    changeName = { profileViewModel.changeName(it) },
                                                    changeDailyTarget = {
                                                        profileViewModel.changeDailyTarget(
                                                            it
                                                        )
                                                    }
                                                ) {
                                                    loginViewModel.launchApp()
                                                }
                                            }
                                            AnimatedVisibility(
                                                visible = currentScreen == main,
                                                enter = slideInHorizontally(tween(300)),
                                                exit = slideOutHorizontally(tween(300))
                                            ) {
                                                MainScreen(
                                                    goToProfile = { currentScreen = profile },
                                                    time = meditationViewModel.time,
                                                    music = meditationViewModel.music,
                                                    isFirstTime = isFirst,
                                                    doFirstTimeLaunch = {
                                                        notificationPermissionState.launchPermissionRequest()
                                                        RemindersManager(context).startReminder("$notificationTime:00")
                                                        DaysManager(context).startCounter()
                                                        WeeksManager(context).startCounter()
                                                    },
                                                    name = name,
                                                    minutesToday = todayMinutes,
                                                    dailyTarget = dailyTarget,
                                                    meditation = meditationViewModel.meditation,
                                                    isMusic = meditationViewModel.isMusic,
                                                    changeMusicStatus = {meditationViewModel.isMusic = it},
                                                    changeMeditationStatus = {meditationViewModel.isMeditation = it},
                                                    changeSoundStatus = {meditationViewModel.isSound = it },
                                                    isSound = meditationViewModel.isSound,
                                                    isMeditation = meditationViewModel.isMeditation,
                                                    sound = meditationViewModel.sound,
                                                    startMeditation = {
                                                        meditationViewModel.startMeditation(context)
                                                        currentScreen = meditation
                                                    },
                                                    chooseMeditation = {meditationViewModel.meditation = it},
                                                    chooseMusic = {meditationViewModel.music = it},
                                                    chooseSound = {meditationViewModel.sound = it },
                                                    chooseTime = {meditationViewModel.time = it},
                                                    startPreview = {
                                                        meditationViewModel.previewRaw = it
                                                        meditationViewModel.startPreview(context)
                                                        meditationViewModel.isPreviewPlaying = true
                                                    },
                                                    stopPreview = {
                                                        meditationViewModel.stopPreview()
                                                        meditationViewModel.isPreviewPlaying = false
                                                    },
                                                    isPlaying = meditationViewModel.isPreviewPlaying,
                                                    previewRaw = meditationViewModel.previewRaw
                                                )
                                            }
                                            AnimatedVisibility(
                                                visible = currentScreen == meditation,
                                                enter = fadeIn(tween(500)),
                                                exit = ExitTransition.None
                                            ) {
                                                MeditationScreen(
                                                    time = meditationViewModel.time,
                                                    changeTodayMinutes = {
                                                        profileViewModel.changeTodayMinutes(
                                                            minutes + 1
                                                        )
                                                        statisticsViewModel.addMinutes(1)
                                                    }) {
                                                    meditationViewModel.stopMeditation()
                                                    currentScreen = main
                                                }
                                            }
                                            AnimatedVisibility(
                                                visible = currentScreen == profile,
                                                enter = EnterTransition.None,
                                                exit = ExitTransition.None
                                            ) {
                                                ProfileScreen(
                                                    goBack = { currentScreen = main },
                                                    changeDailyTarget = {
                                                        profileViewModel.changeDailyTarget(
                                                            it
                                                        )
                                                    },
                                                    time = notificationTime,
                                                    notifications = notifications,
                                                    dailyTarget = dailyTarget,
                                                    name = name,
                                                    averageLastWeek = lastWeekStats,
                                                    average = statisticsViewModel.getThisWeekStatistics(),
                                                    changeNotifications = {profileViewModel.changeNotifications(it)},
                                                    changeNotificationsTime = {
                                                        RemindersManager(context).stopReminder()
                                                        RemindersManager(context).startReminder("$it:00")
                                                        notificationViewModel.changeNotificationsTime(it)}
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}