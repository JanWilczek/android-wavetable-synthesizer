package com.thewolfsound.wavetablesynthesizer

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NativeWavetableSynthesizer : WavetableSynthesizer, DefaultLifecycleObserver {

  private var synthesizerHandle: Long = 0
  private val synthesizerMutex = Object()
  private external fun create(): Long
  private external fun delete(synthesizerHandle: Long)
  private external fun play(synthesizerHandle: Long)
  private external fun stop(synthesizerHandle: Long)
  private external fun isPlaying(synthesizerHandle: Long): Boolean
  private external fun setFrequency(synthesizerHandle: Long, frequencyInHz: Float)
  private external fun setLeftVolume(synthesizerHandle: Long, amplitudeInDb: Float)
  private external fun setRightVolume(synthesizerHandle: Long, amplitudeInDb: Float)
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

  override suspend fun play() = withContext(Dispatchers.Default) {
    synchronized(synthesizerMutex) {
      createNativeHandleIfNotExists()
      play(synthesizerHandle)
    }
  }

  override suspend fun stop() = withContext(Dispatchers.Default) {
    synchronized(synthesizerMutex) {
      createNativeHandleIfNotExists()
      stop(synthesizerHandle)
    }
  }

  override suspend fun isPlaying(): Boolean = withContext(Dispatchers.Default) {
    synchronized(synthesizerMutex) {
      createNativeHandleIfNotExists()
      return@withContext isPlaying(synthesizerHandle)
    }
  }

  override suspend fun setFrequency(frequencyInHz: Float) = withContext(Dispatchers.Default) {
    synchronized(synthesizerMutex) {
      createNativeHandleIfNotExists()
      setFrequency(synthesizerHandle, frequencyInHz)
    }
  }

  override suspend fun setLeftVolume(volumeInDb: Float) = withContext(Dispatchers.Default) {
    synchronized(synthesizerMutex) {
      createNativeHandleIfNotExists()
      setLeftVolume(synthesizerHandle, volumeInDb)
    }
  }

  override suspend fun setRightVolume(volumeInDb: Float) = withContext(Dispatchers.Default) {
    synchronized(synthesizerMutex) {
      createNativeHandleIfNotExists()
      setRightVolume(synthesizerHandle, volumeInDb)
    }
  }

  override suspend fun setWavetable(wavetable: Wavetable) = withContext(Dispatchers.Default) {
    synchronized(synthesizerMutex) {
      createNativeHandleIfNotExists()
      setWavetable(synthesizerHandle, wavetable.ordinal)
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