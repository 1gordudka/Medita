package com.igordudka.medita.utils.workers.statistics

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.igordudka.medita.dagger.AppModule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class WeeksWorker(ctx: Context, workerParameters: WorkerParameters) : CoroutineWorker(ctx, workerParameters) {

    private val appPreferencesRepository = AppModule.provideAppPreferencesRepository(ctx)

    override suspend fun doWork(): Result {
        val days = runBlocking { appPreferencesRepository.days.first() }
        val minutes = runBlocking { appPreferencesRepository.minutes.first() }
        val lastWeekStats = if (days > 1){
            minutes / days
        }else{
            minutes
        }
        appPreferencesRepository.changeLastWeekStats(lastWeekStats)
        appPreferencesRepository.resetDays()
        appPreferencesRepository.resetMinutes()
        return Result.success()
    }
}