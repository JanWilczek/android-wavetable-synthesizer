#pragma once

#include <memory>
#include <mutex>

namespace wavetablesynthesizer {
class WavetableOscillator;

class AudioPlayer;

constexpr auto samplingRate = 48000;

enum class Wavetable { SINE, TRIANGLE, SQUARE, SAW };

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
  std::shared_ptr<WavetableOscillator> _oscillator;
  std::unique_ptr<AudioPlayer> _audioPlayer;
};
}  // namespace wavetablesynthesizer
