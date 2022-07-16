#pragma once

namespace wavetablesynthesizer {

    enum class Wavetable {
        SINE, TRIANGLE, SQUARE, SAW
    };

    class WavetableSynthesizer {
    public:
        virtual ~WavetableSynthesizer();

        virtual void play();

        virtual void stop();

        virtual bool isPlaying();

        virtual void setFrequency(float frequencyInHz);

        virtual void setVolume(float volumeInDb);

        virtual void setWavetable(Wavetable wavetable);
    };
}
