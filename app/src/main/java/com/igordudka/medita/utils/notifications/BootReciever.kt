package com.igordudka.medita.utils.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.igordudka.medita.dagger.AppModule
import com.igordudka.medita.data.AppPreferencesRepository
import com.igordudka.medita.utils.statistics.DaysManager
import com.igordudka.medita.utils.statistics.WeeksManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class BootReceiver : BroadcastReceiver() {
    /*
    * restart reminders alarms when user's device reboots
    * */
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val appPreferencesRepository = AppModule.provideAppPreferencesRepository(context)
            val time = runBlocking { appPreferencesRepository.notificationTime.first() }
            RemindersManager(context).startReminder("$time:00")
            DaysManager(context).startCounter()
            WeeksManager(context).startCounter()
        }
    }
}