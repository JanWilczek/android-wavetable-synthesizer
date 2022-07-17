#include <cmath>
#include "WavetableOscillator.h"


namespace wavetablesynthesizer {

    std::vector<float> generateSineWaveTable() {
        constexpr auto WAVETABLE_LENGTH = 256;
        const auto PI = std::atanf(1.f) * 4;
        std::vector<float> sineWaveTable = std::vector<float>(WAVETABLE_LENGTH);

        for (auto i = 0; i < WAVETABLE_LENGTH; ++i) {
            sineWaveTable[i] = std::sinf(2 * PI * static_cast<float>(i) / WAVETABLE_LENGTH);
        }

        return sineWaveTable;
    }

    WavetableOscillator::WavetableOscillator(std::vector<float> waveTable, float sampleRate)
            : waveTable{std::move(waveTable)},
              sampleRate{sampleRate} {}

    float WavetableOscillator::getSample() {
        index = std::fmod(index, static_cast<float>(waveTable.size()));
        const auto sample = interpolateLinearly();
        index += indexIncrement;
        return amplitude * sample;
    }

    void WavetableOscillator::setFrequency(float frequency) {
        indexIncrement = frequency * static_cast<float>(waveTable.size())
                         / static_cast<float>(sampleRate);
    }

    void WavetableOscillator::onPlaybackStopped() {
        index = 0.f;
    }

    float WavetableOscillator::interpolateLinearly() const {
        const auto truncatedIndex = static_cast<typename decltype(waveTable)
        ::size_type > (index);
        const auto nextIndex = static_cast<typename decltype(waveTable)
        ::size_type >
        (std::ceil(index)) % waveTable.size();
        const auto nextIndexWeight = index - static_cast<float>(truncatedIndex);
        return waveTable[nextIndex] * nextIndexWeight +
               (1.f - nextIndexWeight) * waveTable[truncatedIndex];
    }

    void WavetableOscillator::setAmplitude(float newAmplitude) {
        amplitude.store(newAmplitude);
    }
}
