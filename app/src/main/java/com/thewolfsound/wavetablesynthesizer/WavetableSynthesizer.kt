package com.thewolfsound.wavetablesynthesizer

enum class Wavetable {
  SINE {
    override fun toResourceString(): Int {
      return R.string.sine
    }
  },

  TRIANGLE {
    override fun toResourceString(): Int {
      return R.string.triangle
    }
  },

  SQUARE {
    override fun toResourceString(): Int {
      return R.string.square
    }
  },

  SAW {
    override fun toResourceString(): Int {
      return R.string.sawtooth
    }
  };

  abstract fun toResourceString(): Int
}

interface WavetableSynthesizer {
  suspend fun play()
  suspend fun stop()
  suspend fun isPlaying() : Boolean
  suspend fun setFrequency(frequencyInHz: Float)
  suspend fun setVolume(volumeInDb: Float)
  suspend fun setWavetable(wavetable: Wavetable)
}
