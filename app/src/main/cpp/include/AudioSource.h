#pragma once

namespace wavetablesynthesizer {
class AudioSource {
 public:
  virtual ~AudioSource() = default;

  virtual std::pair<float, float> getSample() = 0;

  virtual void onPlaybackStopped() = 0;
};
}  // namespace wavetablesynthesizer
