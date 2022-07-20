package com.thewolfsound.wavetablesynthesizer

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thewolfsound.wavetablesynthesizer.ui.theme.WavetableSynthesizerTheme


class MainActivity : ComponentActivity() {

  private val synthesizer = NativeWavetableSynthesizer()
  private val synthesizerViewModel: WavetableSynthesizerViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    lifecycle.addObserver(synthesizer)
    synthesizerViewModel.wavetableSynthesizer = synthesizer
    setContent {
      WavetableSynthesizerTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          WavetableSynthesizerApp(Modifier, synthesizerViewModel)
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.removeObserver(synthesizer)
  }

  override fun onResume() {
    super.onResume()
    synthesizerViewModel.applyParameters()
  }
}

@Composable
fun WavetableSynthesizerApp(
  modifier: Modifier,
  synthesizerViewModel: WavetableSynthesizerViewModel = viewModel()
) {
  WavetableSynthesizerTheme {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top,
    ) {
      WavetableSelectionPanel(modifier, synthesizerViewModel)
      ControlsPanel(modifier, synthesizerViewModel)
    }
  }
}

@Composable
private fun ControlsPanel(
  modifier: Modifier,
  synthesizerViewModel: WavetableSynthesizerViewModel
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight(),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(
      modifier = modifier.fillMaxWidth(0.7f),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      PitchControl(modifier, synthesizerViewModel)
      PlayControl(modifier, synthesizerViewModel)
    }
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()
    ) {
      VolumeControl(modifier, synthesizerViewModel)
    }
  }
}

@Composable
private fun PlayControl(modifier: Modifier, synthesizerViewModel: WavetableSynthesizerViewModel) {
  val playButtonLabel = synthesizerViewModel.playButtonLabel.observeAsState()

  Button(modifier = modifier,
    onClick = {
      synthesizerViewModel.playClicked()
    }) {
    Text(playButtonLabel.value ?: "")
  }
}

@Composable
private fun PitchControl(
  modifier: Modifier,
  synthesizerViewModel: WavetableSynthesizerViewModel
) {
  val frequency = synthesizerViewModel.frequency.observeAsState()
  Text("Frequency")
  Slider(value = frequency.value ?: synthesizerViewModel.frequencyRange.start, onValueChange = {
    synthesizerViewModel.setFrequency(it)
  }, valueRange = synthesizerViewModel.frequencyRange)
  Row(
    horizontalArrangement = Arrangement.Center
  ) {
    Text(text = frequency.value.toString())
    Text(" Hz")
  }
}

@Composable
private fun VolumeControl(modifier: Modifier, synthesizerViewModel: WavetableSynthesizerViewModel) {
  val volume = synthesizerViewModel.volume.observeAsState()
  // The volume slider should take around 1/4 of the screen height
  val screenHeight = LocalConfiguration.current.screenHeightDp
  val sliderHeight = screenHeight / 4


  Icon(imageVector = Icons.Filled.VolumeUp, contentDescription = null)
  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight(0.8f)
      .offset(y = 40.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  )
  {
    Slider(
      value = volume.value ?: synthesizerViewModel.volumeRange.endInclusive,
      onValueChange = { synthesizerViewModel.setVolume(it) },
      modifier = modifier
        .width(sliderHeight.dp)
        .rotate(270f),
      valueRange = synthesizerViewModel.volumeRange
    )
  }
  Icon(imageVector = Icons.Filled.VolumeMute, contentDescription = null)
}

@Composable
private fun WavetableSelectionPanel(
  modifier: Modifier,
  synthesizerViewModel: WavetableSynthesizerViewModel
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight(0.5f),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(
      modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight(),
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text("Wavetable")
      WavetableSelectionButtons(modifier, synthesizerViewModel)
    }
  }
}

@Composable
private fun WavetableSelectionButtons(
  modifier: Modifier,
  synthesizerViewModel: WavetableSynthesizerViewModel
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly
  ) {
    for (wavetable in Wavetable.values()) {
      WavetableButton(
        modifier = modifier,
        representedWavetable = wavetable,
        synthesizerViewModel = synthesizerViewModel
      )
    }
  }
}

@Composable
private fun WavetableButton(
  modifier: Modifier,
  representedWavetable: Wavetable,
  synthesizerViewModel: WavetableSynthesizerViewModel
) {
  Button(modifier = modifier, onClick = {
    synthesizerViewModel.setWavetable(representedWavetable)
  }) {
    Text(representedWavetable.toString())
  }
}

@Preview(showBackground = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 720)
@Composable
fun WavetableSynthesizerPreview() {
  WavetableSynthesizerApp(Modifier, WavetableSynthesizerViewModel())
}

@Preview(showBackground = true, widthDp = 100, heightDp = 200)
@Composable
fun VolumeControlPreview() {
  Column(
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight()
  ) {
    VolumeControl(modifier = Modifier, synthesizerViewModel = WavetableSynthesizerViewModel())
  }
}