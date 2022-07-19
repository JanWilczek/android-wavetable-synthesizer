#pragma once

#include <memory>
#include <mutex>
#include "Wavetable.h"
#include "WavetableFactory.h"

namespace wavetablesynthesizer {
class WavetableOscillator;

class AudioPlayer;

constexpr auto samplingRate = 48000;

class WavetableSynthesizer {
 public:
  WavetableSynthesizer();

  virtual ~WavetableSynthesizer();

  virtual void play();

  virtual void stop();

  virtual bool isPlaying();

  virtual void setFrequency(float frequencyInHz);

  virtual void setVolume(float volumeInDb);

  virtual void setWavetable(Wavetable wavetable);

 private:
  bool _isPlaying = false;
  std::mutex _mutex;
  WavetableFactory _wavetableFactory;
  Wavetable _currentWavetable{Wavetable::SINE};
  std::shared_ptr<WavetableOscillator> _oscillator;
  std::unique_ptr<AudioPlayer> _audioPlayer;
};
}  // namespace wavetablesynthesizer
