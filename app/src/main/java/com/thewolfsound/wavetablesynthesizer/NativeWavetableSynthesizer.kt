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
  private external fun setWavetable(synthesizerHandle: Long, wavetable: Int)

  companion object {
    init {
      System.loadLibrary("wavetablesynthesizer")
    }
  }

  override fun onResume(owner: LifecycleOwner) {
    super.onResume(owner)

    synchronized(synthesizerMutex) {
      Log.d("NativeWavetableSynthesizer", "onResume() called")
      createNativeHandleIfNotExists()
    }
  }

  override fun onPause(owner: LifecycleOwner) {
    super.onPause(owner)

    synchronized(synthesizerMutex) {
      Log.d("NativeWavetableSynthesizer", "onPause() called")

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
      createNativeHandleIfNotExists()
      play(synthesizerHandle)
    }
  }

  override fun stop() {
    synchronized(synthesizerMutex) {
      createNativeHandleIfNotExists()
      stop(synthesizerHandle)
    }
  }

  override fun isPlaying(): Boolean {
    synchronized(synthesizerMutex) {
      createNativeHandleIfNotExists()
      return isPlaying(synthesizerHandle)
    }
  }

  override fun setFrequency(frequencyInHz: Float) {
    synchronized(synthesizerMutex) {
      createNativeHandleIfNotExists()
      setFrequency(synthesizerHandle, frequencyInHz)
    }
  }

  override fun setVolume(volumeInDb: Float) {
    synchronized(synthesizerMutex) {
      createNativeHandleIfNotExists()
      setVolume(synthesizerHandle, volumeInDb)
    }
  }

  override fun setWavetable(wavetable: Wavetable) {
    synchronized(synthesizerMutex) {
      createNativeHandleIfNotExists()
      setWavetable(synthesizerHandle, wavetable.ordinal);
    }
  }

  private fun createNativeHandleIfNotExists() {
    if (synthesizerHandle != 0L) {
      return
    }

    // create the synthesizer
    synthesizerHandle = create()
  }
}