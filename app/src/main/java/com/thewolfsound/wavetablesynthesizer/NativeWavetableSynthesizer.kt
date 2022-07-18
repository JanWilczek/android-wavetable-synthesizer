package com.thewolfsound.wavetablesynthesizer

import android.util.Log
import androidx.lifecycle.*

class NativeWavetableSynthesizer : WavetableSynthesizer, DefaultLifecycleObserver {

  private var synthesizerHandle: Long = 0
  private val synthesizerMutex = Object()
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

    synchronized(synthesizerMutex) {
      Log.d("NativeWavetableSynthesizer", "onStart() called")
      if (synthesizerHandle != 0L) {
        Log.e("NativeWavetableSynthesizer", "Attempting to create a new synthesizer while the old one is still alive.")
        delete(synthesizerHandle)
      }

      // create the synthesizer
      synthesizerHandle = create()
    }
  }

  override fun onStop(owner: LifecycleOwner) {
    super.onStop(owner)

    synchronized(synthesizerMutex) {
      Log.d("NativeWavetableSynthesizer", "onStop() called")

      if (synthesizerHandle == 0L) {
        Log.e("NativeWavetableSynthesizer", "Attempting to destroy a null synthesizer.")
        return
      }

      // Destroy the synthesizer
      delete(synthesizerHandle)
      synthesizerHandle = 0L
    }
  }

  override fun play() {
    synchronized(synthesizerMutex) {
      play(synthesizerHandle)
    }
  }

  override fun stop() {
    synchronized(synthesizerMutex) {
      stop(synthesizerHandle)
    }
  }

  override fun isPlaying(): Boolean {
    synchronized(synthesizerMutex) {
      return isPlaying(synthesizerHandle)
    }
  }

  override fun setFrequency(frequencyInHz: Float) {
    synchronized(synthesizerMutex) {
      setFrequency(synthesizerHandle, frequencyInHz)
    }
  }

  override fun setVolume(volumeInDb: Float) {
    synchronized(synthesizerMutex) {
      setVolume(synthesizerHandle, volumeInDb)
    }
  }

  override fun setWavetable(wavetable: Wavetable) {
    synchronized(synthesizerMutex) {
      TODO("Not yet implemented")
    }
  }
}