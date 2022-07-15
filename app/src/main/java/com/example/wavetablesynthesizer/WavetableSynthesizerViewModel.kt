package com.example.wavetablesynthesizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class WavetableSynthesizerViewModel(
  private val wavetableSynthesizer: WavetableSynthesizer
) : ViewModel() {

  private val _frequency = MutableLiveData(100f)
  val frequency: LiveData<Float>
    get() {
      return _frequency
    }

  fun setFrequency(frequencyInHz: Float) {
    _frequency.value = frequencyInHz
    wavetableSynthesizer.setFrequency(frequencyInHz)
  }
}

class WavetableSynthesizerViewModelFactory(
  private val wavetableSynthesizer: WavetableSynthesizer
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return WavetableSynthesizerViewModel(wavetableSynthesizer) as T
  }
}