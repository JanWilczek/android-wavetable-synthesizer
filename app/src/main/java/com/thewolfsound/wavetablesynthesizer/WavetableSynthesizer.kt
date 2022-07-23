package com.thewolfsound.wavetablesynthesizer

enum class Wavetable {
  SINE, TRIANGLE, SQUARE, SAW
}

interface WavetableSynthesizer {
  suspend fun play()
  suspend fun stop()
  suspend fun isPlaying() : Boolean
  suspend fun setFrequency(frequencyInHz: Float)
  suspend fun setVolume(volumeInDb: Float)
  suspend fun setWavetable(wavetable: Wavetable)
}
