package com.thewolfsound.wavetablesynthesizer

class NativeWavetableSynthesizer : WavetableSynthesizer {

  private var synthesizerHandle: Long = 0
  private external fun create(): Long
  private external fun delete(synthesizerHandle: Long)
  private external fun play(synthesizerHandle: Long)
  private external fun stop(synthesizerHandle: Long)
  private external fun isPlaying(synthesizerHandle: Long): Boolean
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
    play(synthesizerHandle)
  }

  override fun stop() {
    stop(synthesizerHandle)
  }

  override fun isPlaying(): Boolean {
    return isPlaying(synthesizerHandle)
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