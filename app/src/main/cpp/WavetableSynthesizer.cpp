#include "Log.h"
#include "WavetableSynthesizer.h"

namespace wavetablesynthesizer {
    WavetableSynthesizer::~WavetableSynthesizer() = default;

    bool WavetableSynthesizer::isPlaying() {}

    void WavetableSynthesizer::play() {}

    void WavetableSynthesizer::setFrequency(float frequencyInHz) {
        LOGD("Frequency set to %.2f Hz.", frequencyInHz);
    }

    void WavetableSynthesizer::setVolume(float volumeInDb) {}

    void WavetableSynthesizer::setWavetable(Wavetable wavetable) {}

    void WavetableSynthesizer::stop() {}
}
