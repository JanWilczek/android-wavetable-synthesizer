#pragma once

#include <android/log.h>

#define LOGD(args...) \
__android_log_print(android_LogPriority::ANDROID_LOG_DEBUG, "WavetableSynthesizer", args)
