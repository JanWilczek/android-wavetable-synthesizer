#include "Log.h"
#include "WavetableSynthesizer.h"
#include "WavetableOscillator.h"
#include "OboeAudioPlayer.h"

namespace wavetablesynthesizer {
    WavetableSynthesizer::WavetableSynthesizer()
        : _oscillator{std::make_shared<WavetableOscillator>(generateSineWaveTable(), samplingRate)}
        , _audioPlayer{std::make_unique<OboeAudioPlayer>(_oscillator, samplingRate)}
    {}

    WavetableSynthesizer::~WavetableSynthesizer() = default;

    bool WavetableSynthesizer::isPlaying() {
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

    void WavetableSynthesizer::setVolume(float volumeInDb) {}

    void WavetableSynthesizer::setWavetable(Wavetable wavetable) {}

    void WavetableSynthesizer::stop() {
        LOGD("stop() called");
        std::lock_guard<std::mutex> lock(_mutex);
        _audioPlayer->stop();
        _isPlaying = false;
    }
}
