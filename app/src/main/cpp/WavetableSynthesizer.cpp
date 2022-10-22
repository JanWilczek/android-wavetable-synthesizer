#include "WavetableSynthesizer.h"
#include <cmath>
#include "Log.h"
#include "OboeAudioPlayer.h"
#include "WavetableOscillator.h"

namespace wavetablesynthesizer {
float dBToAmplitude(float dB) {
  return std::pow(10.f, dB / 20.f);
}

WavetableSynthesizer::WavetableSynthesizer()
    : _oscillator{std::make_shared<WavetableOscillator>(_wavetableFactory.getWaveTable(_currentWavetable), samplingRate)},
      _audioPlayer{
          std::make_unique<OboeAudioPlayer>(_oscillator, samplingRate)} {}

WavetableSynthesizer::~WavetableSynthesizer() = default;

bool WavetableSynthesizer::isPlaying() const {
  LOGD("isPlaying() called");
  return _isPlaying;
}

void WavetableSynthesizer::play() {
  LOGD("play() called");
  std::lock_guard<std::mutex> lock(_mutex);
  const auto result = _audioPlayer->play();
  if (result == 0) {
    _isPlaying = true;
  } else {
    LOGD("Could not start playback.");
  }
}

void WavetableSynthesizer::setFrequency(float frequencyInHz) {
  LOGD("Frequency set to %.2f Hz.", frequencyInHz);
  _oscillator->setFrequency(frequencyInHz);
}

void WavetableSynthesizer::setVolume(float volumeInDb) {
  LOGD("Volume set to %.2f dB.", volumeInDb);
  const auto amplitude = dBToAmplitude(volumeInDb);
  _oscillator->setAmplitude(amplitude);
}

void WavetableSynthesizer::setWavetable(Wavetable wavetable) {
    if (_currentWavetable != wavetable) {
        _currentWavetable = wavetable;
        _oscillator->setWavetable(_wavetableFactory.getWaveTable(wavetable));
    }
}

void WavetableSynthesizer::stop() {
  LOGD("stop() called");
  std::lock_guard<std::mutex> lock(_mutex);
  _audioPlayer->stop();
  _isPlaying = false;
}
}  // namespace wavetablesynthesizer
