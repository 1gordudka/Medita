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
import com.igordudka.medita.ui.viewmodels.allAudio
import com.igordudka.medita.utils.notifications.RemindersManager
import com.igordudka.medita.utils.workers.statistics.DaysWorker
import com.igordudka.medita.utils.workers.statistics.WeeksWorker
import java.util.concurrent.TimeUnit

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
                                                    goToMeditation = { currentScreen = meditation },
                                                    time = meditationViewModel.time,
                                                    music = meditationViewModel.music,
                                                    isFirstTime = isFirst,
                                                    doFirstTimeLaunch = {
                                                        notificationPermissionState.launchPermissionRequest()
                                                        RemindersManager(context).startReminder("$notificationTime:00")
                                                        val dailyWorkRequest = PeriodicWorkRequestBuilder<DaysWorker>(1, TimeUnit.DAYS).build()
                                                        val weeklyWorkRequest = PeriodicWorkRequestBuilder<WeeksWorker>(7, TimeUnit.DAYS).setInitialDelay(7, TimeUnit.DAYS)
                                                            .build()
                                                        WorkManager.getInstance(context).enqueueUniquePeriodicWork("daily_worker", ExistingPeriodicWorkPolicy.REPLACE,
                                                        dailyWorkRequest)
                                                        WorkManager.getInstance(context).enqueueUniquePeriodicWork("weekly_worker",
                                                        ExistingPeriodicWorkPolicy.REPLACE, weeklyWorkRequest)
                                                    },
                                                    name = name,
                                                    minutesToday = todayMinutes,
                                                    dailyTarget = dailyTarget,
                                                    startMeditation = {it1, it2 ->
                                                        meditationViewModel.time = it1
                                                        meditationViewModel.music = it2
                                                        if (it2 != allAudio.size){
                                                            meditationViewModel.startMusic(context = context)
                                                        }
                                                        currentScreen = meditation
                                                    }
                                                )
                                            }
                                            AnimatedVisibility(
                                                visible = currentScreen == meditation,
                                                enter = fadeIn(tween(500)),
                                                exit = ExitTransition.None
                                            ) {
                                                MeditationScreen(
                                                    goBack = { currentScreen = main },
                                                    time = meditationViewModel.time,
                                                    music = meditationViewModel.music,
                                                    changeTodayMinutes = {
                                                        profileViewModel.changeTodayMinutes(
                                                            minutes + 1
                                                        )
                                                        statisticsViewModel.addMinutes(1)
                                                    }) {
                                                    meditationViewModel.stopMusic()
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