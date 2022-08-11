package com.thewolfsound.wavetablesynthesizer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = WolfSoundOrange,
    primaryVariant = WolfSoundDarkOrange,
    secondary = WolfSoundGray
)

private val LightColorPalette = lightColors(
    primary = WolfSoundOrange,
    primaryVariant = WolfSoundDarkOrange,
    secondary = WolfSoundGray
)

@Composable
fun WavetableSynthesizerTheme(darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit) {
  val colors = if (darkTheme) {
    DarkColorPalette
  } else {
    LightColorPalette
  }

  MaterialTheme(
      colors = colors,
      typography = Typography,
      shapes = Shapes,
      content = content
  )
}