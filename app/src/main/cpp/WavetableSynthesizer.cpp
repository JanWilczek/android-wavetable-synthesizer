#include "Log.h"
#include "WavetableSynthesizer.h"

namespace wavetablesynthesizer {
    WavetableSynthesizer::~WavetableSynthesizer() = default;

    bool WavetableSynthesizer::isPlaying() {
        LOGD("isPlaying() called");
        return _isPlaying;
    }

    void WavetableSynthesizer::play() {
        LOGD("play() called");
        _isPlaying = true;
    }

    void WavetableSynthesizer::setFrequency(float frequencyInHz) {
        LOGD("Frequency set to %.2f Hz.", frequencyInHz);
    }

    void WavetableSynthesizer::setVolume(float volumeInDb) {}

    void WavetableSynthesizer::setWavetable(Wavetable wavetable) {}

    void WavetableSynthesizer::stop() {
        LOGD("stop() called");
        _isPlaying = false;
    }
}
