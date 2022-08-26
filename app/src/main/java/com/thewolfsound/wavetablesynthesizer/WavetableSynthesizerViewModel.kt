package com.thewolfsound.wavetablesynthesizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.math.exp
import kotlin.math.ln


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
  fun setFrequencySliderPosition(frequencySliderPosition: Float) {
    val frequencyInHz = frequencyInHzFromSliderPosition(frequencySliderPosition)
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

  fun frequencyInHzFromSliderPosition(sliderPosition: Float): Float {
    val rangePosition = if (sliderPosition == 0F) 0F else exp(ln(0.001F) - ln(0.001F) * sliderPosition)
    return frequencyRange.start + (frequencyRange.endInclusive - frequencyRange.start) * rangePosition
  }

  fun sliderPositionFromFrequencyInHz(frequencyInHz: Float): Float {
    val rangePosition = (frequencyInHz - frequencyRange.start) / (frequencyRange.endInclusive - frequencyRange.start)
    return (ln(rangePosition) - ln(0.001F)) / (-ln(0.001F))
  }

  private val _playButtonLabel = MutableLiveData(R.string.play)
  val playButtonLabel: LiveData<Int>
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
    viewModelScope.launch {
      if (wavetableSynthesizer?.isPlaying() == true) {
        _playButtonLabel.value = R.string.stop
      } else {
        _playButtonLabel.value = R.string.play
      }
    }
  }
}

