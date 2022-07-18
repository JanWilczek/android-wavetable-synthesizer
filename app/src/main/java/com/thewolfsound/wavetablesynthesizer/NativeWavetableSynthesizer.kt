package com.thewolfsound.wavetablesynthesizer

import androidx.lifecycle.*

class NativeWavetableSynthesizer : WavetableSynthesizer, DefaultLifecycleObserver {

  private var synthesizerHandle: Long = 0
  private external fun create(): Long
  private external fun delete(synthesizerHandle: Long)
  private external fun play(synthesizerHandle: Long)
  private external fun stop(synthesizerHandle: Long)
  private external fun isPlaying(synthesizerHandle: Long): Boolean
  private external fun setFrequency(synthesizerHandle: Long, frequencyInHz: Float)
  private external fun setVolume(synthesizerHandle: Long, amplitudeInDb: Float)

  companion object {
    init {
      System.loadLibrary("wavetablesynthesizer")
    }
  }

  override fun onStart(owner: LifecycleOwner) {
    super.onStart(owner)

    // create the synthesizer
    synthesizerHandle = create()
  }

  override fun onStop(owner: LifecycleOwner) {
    // Destroy the synthesizer
    delete(synthesizerHandle)
    synthesizerHandle = 0
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
    setVolume(synthesizerHandle, volumeInDb)
  }

  override fun setWavetable(wavetable: Wavetable) {
    TODO("Not yet implemented")
  }
}