#pragma once

#include <vector>
#include "AudioSource.h"

namespace wavetablesynthesizer {

class WavetableOscillator : public AudioSource {
 public:
  WavetableOscillator() = default;
  WavetableOscillator(std::vector<float> waveTable, float sampleRate);

  float getSample() override;

  virtual void setFrequency(float frequency);

  virtual void setAmplitude(float newAmplitude);

  void onPlaybackStopped() override;

  virtual void setWavetable(const std::vector<float> &wavetable);

 private:
  float interpolateLinearly() const;
  void swapWavetableIfNecessary();

  float index = 0.f;
  std::atomic<float> indexIncrement{0.f};
  std::vector<float> waveTable;
  float sampleRate;
  std::atomic<float> amplitude{1.f};

  std::atomic<bool> swapWavetable{false};
  std::vector<float> wavetableToSwap;
  std::atomic<bool> wavetableIsBeingSwapped{false};
};

class A4Oscillator : public WavetableOscillator {
 public:
  explicit A4Oscillator(float sampleRate);

  float getSample() override;

  void setFrequency(float frequency) override {};

  void setAmplitude(float newAmplitude) override {};

  void onPlaybackStopped() override;

  void setWavetable(const std::vector<float> &wavetable) override {};

 private:
  float _phase{0.f};
  float _phaseIncrement{0.f};
};
}  // namespace wavetablesynthesizer
