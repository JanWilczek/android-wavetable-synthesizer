#pragma once

#include <oboe/Oboe.h>

namespace wavetablesynthesizer {
class OboeAudioPlayer : public oboe::AudioStreamDataCallback {
public:
    OboeAudioPlayer();
    oboe::DataCallbackResult onAudioReady(oboe::AudioStream* audioStream, void* audioData, int32_t framesCount) override;

};
}
