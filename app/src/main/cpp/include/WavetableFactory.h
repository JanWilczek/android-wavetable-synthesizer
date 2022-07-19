#pragma once
#include <memory>
#include <vector>
#include "WavetableOscillator.h"

namespace wavetablesynthesizer {
enum class Wavetable;

class WavetableFactory {
public:
  std::vector<float> getWaveTable(Wavetable wavetable);

private:
  std::vector<float> sineWaveTable;
  std::vector<float> triangleWaveTable;
  std::vector<float> squareWaveTable;
  std::vector<float> sawtoothWaveTable;
};
}  // namespace wavetablesynthesizer
