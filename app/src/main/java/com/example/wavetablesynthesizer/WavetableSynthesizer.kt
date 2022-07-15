package com.example.wavetablesynthesizer

enum class Wavetable {
  SINE, TRIANGLE, SQUARE, SAW
}

interface WavetableSynthesizer {
  fun play()
  fun stop()
  fun isPlaying() : Boolean
  fun setFrequency(frequencyInHz: Float)
  fun setVolume(volumeInDb: Float)
  fun setWavetable(wavetable: Wavetable)
}
