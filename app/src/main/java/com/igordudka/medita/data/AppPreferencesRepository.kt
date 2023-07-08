package com.igordudka.medita.data

import android.app.Notification
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val ISFIRST = booleanPreferencesKey("isFirst")
        val DAILYTARGET = intPreferencesKey("dailyTarget")
        val NAME = stringPreferencesKey("Name")
        val TODAYMINUTES = intPreferencesKey("todayMinutes")
        val NOTIFICATIONS = booleanPreferencesKey("notifications")
        val NOTIFICATIONTIME = intPreferencesKey("notificationTime")
        val DAYS = intPreferencesKey("days")
        val MINUTES = intPreferencesKey("minutes")
        val LASTWEEKSTATS = intPreferencesKey("lastWeekStats")
    }

    suspend fun changeNotificationsTime(new: Int){
        dataStore.edit {
            it[NOTIFICATIONTIME] = new
        }
    }

    suspend fun changeDailyTarget(minutes: Int){
        dataStore.edit {
            it[DAILYTARGET] = minutes
        }
    }

    suspend fun changeNotifications(notifications: Boolean){
        dataStore.edit {
            it[NOTIFICATIONS] = notifications
        }
    }

    suspend fun changeName(name: String){
        dataStore.edit {
            it[NAME] = name
        }
    }

    suspend fun changeTodayMinutes(minutes: Int){
        dataStore.edit {
            it[TODAYMINUTES] = minutes
        }
    }

    suspend fun appLaunched() {
        dataStore.edit {
            it[ISFIRST] = false
        }
    }

    suspend fun addDay(days: Int){
        dataStore.edit {
            it[DAYS] = days + 1
        }
    }
    suspend fun resetDays(){
        dataStore.edit {
            it[DAYS] = 0
        }
    }
    suspend fun addMinutes(minutes: Int, amount: Int){
        dataStore.edit {
            it[MINUTES] = minutes + amount
        }
    }
    suspend fun resetMinutes(){
        dataStore.edit {
            it[MINUTES] = 0
        }
    }
    suspend fun changeLastWeekStats(new: Int){
        dataStore.edit {
            it[LASTWEEKSTATS] = new
        }
    }

    val isFirst = dataStore.data.map {
        it[ISFIRST] ?: true
    }
    val dailyTarget = dataStore.data.map {
        it[DAILYTARGET] ?: 0
    }
    val name = dataStore.data.map {
        it[NAME] ?: ""
    }
    val todayMinutes = dataStore.data.map {
        it[TODAYMINUTES] ?: 0
    }
    val notifications = dataStore.data.map {
        it[NOTIFICATIONS] ?: true
    }
    val notificationTime = dataStore.data.map {
        it[NOTIFICATIONTIME] ?: 12
    }
    val days = dataStore.data.map {
        it[DAYS] ?: 0
    }
    val minutes = dataStore.data.map {
        it[MINUTES] ?: 0
    }
    val lastWeekStats = dataStore.data.map {
        it[LASTWEEKSTATS] ?: 0
    }

}