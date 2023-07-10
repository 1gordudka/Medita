package com.igordudka.medita.utils.statistics

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.igordudka.medita.dagger.AppModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DaysReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        val appPreferencesRepository = AppModule.provideAppPreferencesRepository(context)
        val days = runBlocking { appPreferencesRepository.days.first() }
        val todayMinutes = runBlocking { appPreferencesRepository.todayMinutes.first() }
        val minutes = runBlocking { appPreferencesRepository.minutes.first() }
        runBlocking {
            appPreferencesRepository.addMinutes(minutes, todayMinutes)
            appPreferencesRepository.changeTodayMinutes(0)
            appPreferencesRepository.addDay(days) }
        DaysManager(context).startCounter()
    }
}