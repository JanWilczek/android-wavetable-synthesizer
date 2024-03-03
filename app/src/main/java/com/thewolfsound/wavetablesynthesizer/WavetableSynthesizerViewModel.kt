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
  private val frequencyRange = 40f..3000f

  private val _volumeLeft = MutableLiveData(-24f)
  val volumeLeft: LiveData<Float>
    get() {
      return _volumeLeft
    }

  private val _volumeRight = MutableLiveData(-24f)
  val volumeRight: LiveData<Float>
    get() {
      return _volumeRight
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

  fun setVolumeLeft(volumeInDb: Float) {
    _volumeLeft.value = volumeInDb
    viewModelScope.launch {
      wavetableSynthesizer?.setLeftVolume(volumeInDb)
    }
  }

  fun setVolumeRight(volumeInDb: Float) {
    _volumeRight.value = volumeInDb
    viewModelScope.launch {
      wavetableSynthesizer?.setRightVolume(volumeInDb)
    }
  }

  fun setWavetable(newWavetable: Wavetable) {
    wavetable = newWavetable
    viewModelScope.launch {
      wavetableSynthesizer?.setWavetable(newWavetable)
    }
  }

  fun playClicked() {
    // play() and stop() are suspended functions => we must launch a coroutine
    viewModelScope.launch {
      if (wavetableSynthesizer?.isPlaying() == true) {
        wavetableSynthesizer?.stop()
      } else {
        wavetableSynthesizer?.play()
      }
      // Only when the synthesizer changed its state, update the button label.
      updatePlayButtonLabel()
    }
  }

  private fun frequencyInHzFromSliderPosition(sliderPosition: Float): Float {
    val rangePosition = linearToExponential(sliderPosition)
    return valueFromRangePosition(frequencyRange, rangePosition)
  }

  fun sliderPositionFromFrequencyInHz(frequencyInHz: Float): Float {
    val rangePosition = rangePositionFromValue(frequencyRange, frequencyInHz)
    return exponentialToLinear(rangePosition)
  }

  companion object LinearToExponentialConverter {

    private const val MINIMUM_VALUE = 0.001f
    fun linearToExponential(value: Float): Float {
      assert(value in 0f..1f)


      if (value < MINIMUM_VALUE) {
        return 0f
      }

      return exp(ln(MINIMUM_VALUE) - ln(MINIMUM_VALUE) * value)
    }

    fun valueFromRangePosition(
      range: ClosedFloatingPointRange<Float>,
      rangePosition: Float
    ): Float {
      assert(rangePosition in 0f..1f)

      return range.start + (range.endInclusive - range.start) * rangePosition
    }


    fun rangePositionFromValue(range: ClosedFloatingPointRange<Float>, value: Float): Float {
      assert(value in range)

      return (value - range.start) / (range.endInclusive - range.start)
    }


    fun exponentialToLinear(rangePosition: Float): Float {
      assert(rangePosition in 0f..1f)

      if (rangePosition < MINIMUM_VALUE) {
        return rangePosition
      }

      return (ln(rangePosition) - ln(MINIMUM_VALUE)) / (-ln(MINIMUM_VALUE))
    }
  }

  private val _playButtonLabel = MutableLiveData(R.string.play)
  val playButtonLabel: LiveData<Int>
    get() {
      return _playButtonLabel
    }

  fun applyParameters() {
    viewModelScope.launch {
      wavetableSynthesizer?.setFrequency(frequency.value!!)
      wavetableSynthesizer?.setLeftVolume(volumeLeft.value!!)
      wavetableSynthesizer?.setRightVolume(volumeLeft.value!!)
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

