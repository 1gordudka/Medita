package com.igordudka.medita.ui.viewmodels

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.igordudka.medita.MainActivity
import com.igordudka.medita.R
import dagger.hilt.android.qualifiers.ApplicationContext

val allAudio = listOf(R.raw.forest, R.raw.fire, R.raw.water, R.raw.rain, R.raw.space)

class MeditationViewModel : ViewModel() {

    var time by mutableStateOf(-1)
    var music by mutableStateOf(-1)

    private lateinit var mediaPlayer: MediaPlayer

    fun startMusic(
        context: Context
    ){
        mediaPlayer = MediaPlayer.create(context, allAudio[music])
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    fun stopMusic(){
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}