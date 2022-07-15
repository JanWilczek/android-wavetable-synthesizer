package com.example.wavetablesynthesizer

import android.util.Log

class LoggingWavetableSynthesizer : WavetableSynthesizer {

  override fun play() {
    TODO("Not yet implemented")
  }

  override fun stop() {
    TODO("Not yet implemented")
  }

  override fun isPlaying(): Boolean {
    TODO("Not yet implemented")
  }

  override fun setFrequency(frequencyInHz: Float) {
    Log.d("LoggingWavetableSynthesizer", "Frequency set to " + frequencyInHz.toString() + "Hz.")
  }

  override fun setVolume(volumeInDb: Float) {
    TODO("Not yet implemented")
  }

  override fun setWavetable(wavetable: Wavetable) {
    TODO("Not yet implemented")
  }
}