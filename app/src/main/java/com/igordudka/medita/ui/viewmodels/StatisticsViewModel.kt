package com.igordudka.medita.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igordudka.medita.data.AppPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.temporal.TemporalAmount
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val appPreferencesRepository: AppPreferencesRepository)
    : ViewModel() {

    val days = appPreferencesRepository.days.map { it }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            null
        )
    val minutes = appPreferencesRepository.minutes.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )
    val lastWeekStats = appPreferencesRepository.lastWeekStats.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    fun addMinutes(amount: Int){
        viewModelScope.launch {
            minutes.value?.let {
                appPreferencesRepository.addMinutes(it, amount)
            }
        }
    }
    fun getThisWeekStatistics() : Int{
        if (days.value!! > 0){
            return (minutes.value!! / days.value!!).toInt()
        }else{
            return minutes.value!!
        }
    }
    }