package com.igordudka.medita.utils.statistics

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.igordudka.medita.dagger.AppModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class WeeksReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val appPreferencesRepository = AppModule.provideAppPreferencesRepository(context)
        val days = runBlocking { appPreferencesRepository.days.first() }
        val minutes = runBlocking { appPreferencesRepository.minutes.first() }
        val lastWeekStats = if (days > 0){
            minutes / days
        }else{
            minutes
        }
        runBlocking { appPreferencesRepository.changeLastWeekStats(lastWeekStats)
            appPreferencesRepository.resetDays()
            appPreferencesRepository.resetMinutes() }
    }
}