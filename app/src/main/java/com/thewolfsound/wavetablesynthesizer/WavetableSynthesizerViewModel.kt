package com.thewolfsound.wavetablesynthesizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class WavetableSynthesizerViewModel : ViewModel() {

  var wavetableSynthesizer: WavetableSynthesizer? = null
  set(value) {
    field = value
    applyParameters()
  }

  private val _frequency = MutableLiveData(300f)
  val frequency: LiveData<Float>
    get() {
      return _frequency
    }
  val frequencyRange = 60f..6000f

  private val _volume = MutableLiveData(-12f)
  val volume: LiveData<Float>
    get() {
      return _volume
    }
  val volumeRange = (-60f)..0f

  fun setFrequency(frequencyInHz: Float) {
    _frequency.value = frequencyInHz
    wavetableSynthesizer?.setFrequency(frequencyInHz)
  }

  fun setVolume(volumeInDb: Float) {
    _volume.value = volumeInDb
    wavetableSynthesizer?.setVolume(volumeInDb)
  }

  fun setWavetable(wavetable: Wavetable) {
    wavetableSynthesizer?.setWavetable(wavetable)
  }

  fun playClicked() {
    if (wavetableSynthesizer?.isPlaying() == true) {
      wavetableSynthesizer?.stop()
    } else {
      wavetableSynthesizer?.play()
    }
    updatePlayButtonLabel()
  }

  private val _playButtonLabel = MutableLiveData("Play")
  val playButtonLabel: LiveData<String>
    get() {
      return _playButtonLabel
    }

  fun applyParameters() {
    wavetableSynthesizer?.setFrequency(frequency.value!!)
    wavetableSynthesizer?.setVolume(volume.value!!)
//    wavetableSynthesizer?.setWavetable()
    updatePlayButtonLabel()
  }

  private fun updatePlayButtonLabel() {
    if (wavetableSynthesizer?.isPlaying() == true) {
      _playButtonLabel.value = "Stop"
    } else {
      _playButtonLabel.value = "Play"
    }
  }
}

