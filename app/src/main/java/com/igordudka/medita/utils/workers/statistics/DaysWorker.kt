package com.igordudka.medita.utils.workers.statistics

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.igordudka.medita.dagger.AppModule
import com.igordudka.medita.data.AppPreferencesRepository
import com.igordudka.medita.ui.viewmodels.StatisticsViewModel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DaysWorker(ctx: Context, workerParameters: WorkerParameters) : CoroutineWorker(ctx, workerParameters) {

    private val appPreferencesRepository = AppModule.provideAppPreferencesRepository(ctx)

    override suspend fun doWork(): Result {
        val days = runBlocking { appPreferencesRepository.days.first() }
        appPreferencesRepository.changeTodayMinutes(0)
        appPreferencesRepository.addDay(days)
        return Result.success()
    }
}