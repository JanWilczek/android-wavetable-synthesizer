#pragma once

#include <vector>
#include "AudioSource.h"

namespace wavetablesynthesizer {

class WavetableOscillator : public AudioSource {
 public:
  WavetableOscillator() = default;
  WavetableOscillator(std::vector<float> waveTable, float sampleRate);

  std::pair<float, float> getSample() override;

  virtual void setFrequency(float frequency);

  virtual void setAmplitude(float newAmplitude);

  virtual void setLeftAmplitude(float newAmplitude);

  virtual void setRightAmplitude(float newAmplitude);

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
  std::atomic<float> leftAmplitude{1.f};
  std::atomic<float> rightAmplitude{1.f};

  std::atomic<bool> swapWavetable{false};
  std::vector<float> wavetableToSwap;
  std::atomic<bool> wavetableIsBeingSwapped{false};
};
}  // namespace wavetablesynthesizer
