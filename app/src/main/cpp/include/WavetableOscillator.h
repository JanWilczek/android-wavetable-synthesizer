#pragma once
#include <vector>
#include "AudioSource.h"

namespace wavetablesynthesizer {
    std::vector<float> generateSineWaveTable();

    class WavetableOscillator : public AudioSource {
    public:
        WavetableOscillator(std::vector<float> waveTable, float sampleRate);

        float getSample() override;

        void setFrequency(float frequency);

        void setAmplitude(float newAmplitude);

        void onPlaybackStopped() override;

    private:
        float interpolateLinearly() const;

        float index = 0.f;
        std::atomic<float> indexIncrement{0.f};
        std::vector<float> waveTable;
        float sampleRate;
        std::atomic<float> amplitude {1.f};
    };
}
