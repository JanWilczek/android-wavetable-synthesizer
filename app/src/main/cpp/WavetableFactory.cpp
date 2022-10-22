#include "WavetableFactory.h"
#include <cmath>
#include <vector>
#include "Wavetable.h"
#include "MathConstants.h"

namespace wavetablesynthesizer {
static constexpr auto WAVETABLE_LENGTH = 256;

std::vector<float> generateSineWaveTable() {
  auto sineWaveTable = std::vector<float>(WAVETABLE_LENGTH);

  for (auto i = 0; i < WAVETABLE_LENGTH; ++i) {
    sineWaveTable[i] =
        std::sinf(2 * PI * static_cast<float>(i) / WAVETABLE_LENGTH);
  }

  return sineWaveTable;
}

std::vector<float> generateTriangleWaveTable() {
  auto triangleWaveTable = std::vector<float>(WAVETABLE_LENGTH, 0.f);

  constexpr auto HARMONICS_COUNT = 13;

  for (auto k = 1; k <= HARMONICS_COUNT; ++k) {
    for (auto j = 0; j < WAVETABLE_LENGTH; ++j) {
      const auto phase = 2.f * PI * 1.f * j / WAVETABLE_LENGTH;
      triangleWaveTable[j] += 8.f / std::pow(PI, 2.f) * std::pow(-1.f, k) *
                              std::pow(2 * k - 1, -2.f) *
                              std::sin((2.f * k - 1.f) * phase);
    }
  }

  return triangleWaveTable;
}

std::vector<float> generateSquareWaveTable() {
  auto squareWaveTable = std::vector<float>(WAVETABLE_LENGTH, 0.f);

  constexpr auto HARMONICS_COUNT = 7;

  for (auto k = 1; k <= HARMONICS_COUNT; ++k) {
    for (auto j = 0; j < WAVETABLE_LENGTH; ++j) {
      const auto phase = 2.f * PI * 1.f * j / WAVETABLE_LENGTH;
      squareWaveTable[j] += 4.f / PI * std::pow(2.f * k - 1.f, -1.f) *
                            std::sin((2.f * k - 1.f) * phase);
    }
  }

  return squareWaveTable;
}

std::vector<float> generateSawWaveTable() {
  auto sawWaveTable = std::vector<float>(WAVETABLE_LENGTH, 0.f);

  constexpr auto HARMONICS_COUNT = 26;

  for (auto k = 1; k <= HARMONICS_COUNT; ++k) {
    for (auto j = 0; j < WAVETABLE_LENGTH; ++j) {
      const auto phase = 2.f * PI * 1.f * j / WAVETABLE_LENGTH;
      sawWaveTable[j] += 2.f / PI * std::pow(-1.f, k) * std::pow(k, -1.f) *
                         std::sin(k * phase);
    }
  }

  return sawWaveTable;
}

std::vector<float> WavetableFactory::getWaveTable(Wavetable wavetable) {
  switch (wavetable) {
    case Wavetable::SINE:
      return sineWaveTable();
    case Wavetable::TRIANGLE:
      return triangleWaveTable();
    case Wavetable::SQUARE:
      return squareWaveTable();
    case Wavetable::SAW:
      return sawWaveTable();
    default:
      return {WAVETABLE_LENGTH, 0.f};
  }
}

template <typename F>
std::vector<float> generateWaveTableOnce(std::vector<float>& waveTable,
                                         F&& generator) {
  if (waveTable.empty()) {
    waveTable = generator();
  }

  return waveTable;
}

std::vector<float> WavetableFactory::sineWaveTable() {
  return generateWaveTableOnce(_sineWaveTable, &generateSineWaveTable);
}

std::vector<float> WavetableFactory::triangleWaveTable() {
  return generateWaveTableOnce(_triangleWaveTable, &generateTriangleWaveTable);
}

std::vector<float> WavetableFactory::squareWaveTable() {
  return generateWaveTableOnce(_squareWaveTable, &generateSquareWaveTable);
}

std::vector<float> WavetableFactory::sawWaveTable() {
  return generateWaveTableOnce(_sawWaveTable, &generateSawWaveTable);
}
}  // namespace wavetablesynthesizer