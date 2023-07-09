package com.igordudka.medita.ui.viewmodels

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igordudka.medita.MainActivity
import com.igordudka.medita.R
import com.igordudka.medita.ui.meditation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val allMeditations = listOf(Meditation(R.drawable.med_breath_and_body, R.raw.med_breath_and_body),
Meditation(R.drawable.med_dance_of_gratitude, R.raw.med_dance_of_gratitude), Meditation(R.drawable.med_heart_center, R.raw.med_heart_center),
Meditation(R.drawable.med_self_discovery, R.raw.med_self_discovery), Meditation(R.drawable.med_way_of_change, R.raw.med_way_of_change)
)
val allSounds = listOf(Sound(R.drawable.sound_fire, R.raw.sound_fire), Sound(R.drawable.sound_forest, R.raw.sound_forest),
Sound(R.drawable.sound_rain, R.raw.sound_rain), Sound(R.drawable.sound_space, R.raw.sound_space), Sound(R.drawable.sound_waterfall,
    R.raw.sound_water)
)
val allMusic = listOf(Music(R.drawable.music_daytime, R.raw.music_daytime), Music(R.drawable.music_dreams, R.raw.music_dreams), Music(R.drawable.music_moon,
R.raw.music_moon), Music(R.drawable.music_relax, R.raw.music_relax), Music(R.drawable.music_wonder, R.raw.music_wonder)
)


class MeditationViewModel : ViewModel() {

    var time by mutableStateOf(-1)
    var sound by mutableStateOf(0)
    var music by mutableStateOf(0)
    var meditation by mutableStateOf(0)

    var isMeditation by mutableStateOf(true)
    var isSound by mutableStateOf(false)
    var isMusic by mutableStateOf(true)

    private lateinit var soundMediaPlayer: MediaPlayer
    private lateinit var meditationMediaPlayer: MediaPlayer
    private lateinit var musicMediaPlayer: MediaPlayer

    fun startMeditation(context: Context){
        viewModelScope.launch {
            if (isSound){
                startSound(context)
            }
            if (isMusic){
                startMusic(context)
            }
            if (isMeditation){
                delay(12000)
                startMeditationSound(context)
            }
        }

    }

    fun stopMeditation(){
        if (isSound){
            stopSound()
        }
        if (isMeditation){
            stopMeditationSound()
        }
        if (isMusic){
            stopMusic()
        }
    }

    private fun startSound(
        context: Context
    ){
        soundMediaPlayer = MediaPlayer.create(context, allSounds[sound].raw)
        soundMediaPlayer.isLooping = true
        soundMediaPlayer.setVolume(0.7f, 0.7f)
        soundMediaPlayer.start()
    }
    private fun stopSound(){
        soundMediaPlayer.stop()
        soundMediaPlayer.release()
    }

    private fun startMeditationSound(context: Context){
        meditationMediaPlayer = MediaPlayer.create(context, allMeditations[meditation].raw)
        meditationMediaPlayer.setVolume(1f, 1f)
        meditationMediaPlayer.start()
    }
    private fun stopMeditationSound(){
        meditationMediaPlayer.stop()
        meditationMediaPlayer.release()
    }

    private fun startMusic(context: Context){
        musicMediaPlayer = MediaPlayer.create(context, allMusic[music].raw)
        musicMediaPlayer.isLooping = true
        musicMediaPlayer.setVolume(0.6f, 0.6f)
        musicMediaPlayer.start()
    }
    private fun stopMusic(){
        musicMediaPlayer.stop()
        musicMediaPlayer.release()
    }
}

data class Meditation(
    val pic: Int,
    val raw: Int
)

data class Sound(
    val pic: Int,
    val raw: Int
)

data class Music(
    val pic: Int,
    val raw: Int
)