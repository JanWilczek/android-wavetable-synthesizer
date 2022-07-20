package com.thewolfsound.wavetablesynthesizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.log


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
  val frequencyRange = 40f..3000f

  private val _volume = MutableLiveData(-24f)
  val volume: LiveData<Float>
    get() {
      return _volume
    }
  val volumeRange = (-60f)..0f

  private var wavetable = Wavetable.SINE

  /**
   * @param frequencySliderPosition slider position in [0, 1] range
   */
  fun setFrequencySlierPosition(frequencySliderPosition: Float) {
    val rangePosition = if (frequencySliderPosition == 0F) 0F else exp(ln(0.001F) - ln(0.001F) * frequencySliderPosition)
    val frequencyInHz = frequencyRange.start + (frequencyRange.endInclusive - frequencyRange.start) * rangePosition
    _frequency.value = frequencyInHz
    viewModelScope.launch {
      wavetableSynthesizer?.setFrequency(frequencyInHz)
    }
  }

  fun setVolume(volumeInDb: Float) {
    _volume.value = volumeInDb
    viewModelScope.launch {
      wavetableSynthesizer?.setVolume(volumeInDb)
    }
  }

  fun setWavetable(newWavetable: Wavetable) {
    wavetable = newWavetable
    viewModelScope.launch {
      wavetableSynthesizer?.setWavetable(newWavetable)
    }
  }

  fun playClicked() {
    viewModelScope.launch {
      if (wavetableSynthesizer?.isPlaying() == true) {
        wavetableSynthesizer?.stop()
      } else {
        wavetableSynthesizer?.play()
      }
      updatePlayButtonLabel()
    }
  }

  private val _playButtonLabel = MutableLiveData("Play")
  val playButtonLabel: LiveData<String>
    get() {
      return _playButtonLabel
    }

  fun applyParameters() {
    viewModelScope.launch {
      wavetableSynthesizer?.setFrequency(frequency.value!!)
      wavetableSynthesizer?.setVolume(volume.value!!)
      wavetableSynthesizer?.setWavetable(wavetable)
      updatePlayButtonLabel()
    }
  }

  private fun updatePlayButtonLabel() {
    if (wavetableSynthesizer?.isPlaying() == true) {
      _playButtonLabel.value = "Stop"
    } else {
      _playButtonLabel.value = "Play"
    }
  }
}

