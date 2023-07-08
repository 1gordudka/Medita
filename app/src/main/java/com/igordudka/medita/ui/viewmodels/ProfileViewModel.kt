package com.igordudka.medita.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igordudka.medita.data.AppPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
): ViewModel() {

    val name = appPreferencesRepository.name.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )
    val dailyTarget = appPreferencesRepository.dailyTarget.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )
    val todayMinutes = appPreferencesRepository.todayMinutes.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )
    val notifications = appPreferencesRepository.notifications.map { it}.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    fun changeNotifications(notifications: Boolean){
        viewModelScope.launch {
            appPreferencesRepository.changeNotifications(notifications)
        }
    }

    fun changeName(name: String){
        viewModelScope.launch {
            appPreferencesRepository.changeName(name)
        }
    }
    fun changeDailyTarget(minutes: Int){
        viewModelScope.launch {
            appPreferencesRepository.changeDailyTarget(minutes)
        }
    }
    fun changeTodayMinutes(minutes: Int){
        viewModelScope.launch {
            appPreferencesRepository.changeTodayMinutes(minutes)
        }
    }
}