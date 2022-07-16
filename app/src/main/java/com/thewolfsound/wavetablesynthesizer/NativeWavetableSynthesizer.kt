package com.thewolfsound.wavetablesynthesizer

class NativeWavetableSynthesizer : WavetableSynthesizer {

  private var synthesizerHandle: Long = 0
  private external fun create(): Long
  private external fun delete(synthesizerHandle: Long)
  private external fun setFrequency(synthesizerHandle: Long, frequencyInHz: Float)

  companion object {
    init {
      System.loadLibrary("wavetablesynthesizer")
    }
  }

  init {
    // Create the synthesizer
    synthesizerHandle = create()
  }

  protected fun finalize() {
    // Destroy the synthesizer
    delete(synthesizerHandle)
  }

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
    setFrequency(synthesizerHandle, frequencyInHz)
  }

  override fun setVolume(volumeInDb: Float) {
    TODO("Not yet implemented")
  }

  override fun setWavetable(wavetable: Wavetable) {
    TODO("Not yet implemented")
  }
}