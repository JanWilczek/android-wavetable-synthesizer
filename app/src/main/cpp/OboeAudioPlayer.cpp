#include "OboeAudioPlayer.h"

using namespace oboe;

namespace wavetablesynthesizer {
    OboeAudioPlayer::OboeAudioPlayer() {
        AudioStreamBuilder builder;
        builder.setPerformanceMode(PerformanceMode::LowLatency)
        ->setDirection(Direction::Output)
        ->setDataCallback(this)
        ->setSharingMode(SharingMode::Exclusive)
        ->setFormat(AudioFormat::Float)
        ->setChannelCount(ChannelCount::Mono);
    }

    DataCallbackResult
    OboeAudioPlayer::onAudioReady(oboe::AudioStream *audioStream, void *audioData,
                                 int32_t framesCount) {

        return oboe::DataCallbackResult::Continue;
    }
}
