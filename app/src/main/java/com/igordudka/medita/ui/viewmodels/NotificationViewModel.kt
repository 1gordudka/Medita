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
class NotificationViewModel @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository
): ViewModel() {

    val notificationTime = appPreferencesRepository.notificationTime.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    fun changeNotificationsTime(new: Int){
        viewModelScope.launch {
            appPreferencesRepository.changeNotificationsTime(new)
        }
    }
}