package com.example.wavetablesynthesizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WavetableSynthesizerViewModel(
  private val wavetableSynthesizer: WavetableSynthesizer
) : ViewModel() {

  private val _frequency = MutableLiveData(100f)
  val frequency: LiveData<Float>
    get() {
      return _frequency
    }
  val frequencyRange = 16f..16000f

  private val _volume = MutableLiveData(0f)
  val volume: LiveData<Float>
    get() {
      return _volume
    }
  val volumeRange = (-60f)..0f

  fun setFrequency(frequencyInHz: Float) {
    _frequency.value = frequencyInHz
    wavetableSynthesizer.setFrequency(frequencyInHz)
  }

  fun setVolume(volumeInDb: Float) {
    _volume.value = volumeInDb
    wavetableSynthesizer.setVolume(volumeInDb)
  }

  fun setWavetable(wavetable: Wavetable) {
    wavetableSynthesizer.setWavetable(wavetable)
  }

  fun playClicked() {
    if (wavetableSynthesizer.isPlaying()) {
      wavetableSynthesizer.stop()
      _playButtonLabel.value = "Play"
    } else {
      wavetableSynthesizer.play()
      _playButtonLabel.value = "Stop"
    }
  }

  private val _playButtonLabel = MutableLiveData("Play")
  val playButtonLabel: LiveData<String>
    get() {
      return _playButtonLabel
    }
}

class WavetableSynthesizerViewModelFactory(
  private val wavetableSynthesizer: WavetableSynthesizer
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return WavetableSynthesizerViewModel(wavetableSynthesizer) as T
  }
}