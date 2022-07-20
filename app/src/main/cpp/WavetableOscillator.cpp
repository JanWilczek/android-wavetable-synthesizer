#include "WavetableOscillator.h"
#include <cmath>

namespace wavetablesynthesizer {

WavetableOscillator::WavetableOscillator(std::vector<float> waveTable,
                                         float sampleRate)
    : waveTable{std::move(waveTable)}, sampleRate{sampleRate} {}

float WavetableOscillator::getSample() {
  swapWavetableIfNecessary();

  index = std::fmod(index, static_cast<float>(waveTable.size()));
  const auto sample = interpolateLinearly();
  index += indexIncrement;
  return amplitude * sample;
}

void WavetableOscillator::swapWavetableIfNecessary() {
  wavetableIsBeingSwapped.store(true, std::memory_order_release);
  if (swapWavetable.load(std::memory_order_acquire)) {
    std::swap(waveTable, wavetableToSwap);
    swapWavetable.store(false, std::memory_order_relaxed);
  }
  wavetableIsBeingSwapped.store(false, std::memory_order_release);
}

void WavetableOscillator::setFrequency(float frequency) {
  indexIncrement = frequency * static_cast<float>(waveTable.size()) /
                   static_cast<float>(sampleRate);
}

void WavetableOscillator::onPlaybackStopped() {
  index = 0.f;
}

float WavetableOscillator::interpolateLinearly() const {
  const auto truncatedIndex =
      static_cast<typename decltype(waveTable)::size_type>(index);
  const auto nextIndex = (truncatedIndex + 1u) % waveTable.size();
  const auto nextIndexWeight = index - static_cast<float>(truncatedIndex);
  return waveTable[nextIndex] * nextIndexWeight +
         (1.f - nextIndexWeight) * waveTable[truncatedIndex];
}

void WavetableOscillator::setAmplitude(float newAmplitude) {
  amplitude.store(newAmplitude);
}

void WavetableOscillator::setWavetable(const std::vector<float>& wavetable) {
  // Wait for the previous swap to take place if the oscillator is playing
  swapWavetable.store(false, std::memory_order_release);
  while (wavetableIsBeingSwapped.load(std::memory_order_acquire)) {
  }
  wavetableToSwap = wavetable;
  swapWavetable.store(true, std::memory_order_release);
}
}  // namespace wavetablesynthesizer
