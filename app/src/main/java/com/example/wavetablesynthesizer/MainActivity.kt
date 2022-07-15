package com.example.wavetablesynthesizer

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wavetablesynthesizer.ui.theme.WavetableSynthesizerTheme


class MainActivity : ComponentActivity() {

  private val synthesizerViewModel: WavetableSynthesizerViewModel by viewModels {
    WavetableSynthesizerViewModelFactory(LoggingWavetableSynthesizer())
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    setContent {
      WavetableSynthesizerTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          WavetableSynthesizerApp(Modifier, synthesizerViewModel)
        }
      }
    }
  }
}


class SynthesizerState(
  val wavetable: MutableState<Wavetable>,
  val volumeInDecibels: MutableState<Float>
) {

}

@Composable
fun rememberSynthesizerState(
  wavetable: MutableState<Wavetable> = remember { mutableStateOf(Wavetable.SINE) },
  volumeInDecibels: MutableState<Float> = remember { mutableStateOf(0f) }
) = remember(wavetable, volumeInDecibels) {
  SynthesizerState(wavetable, volumeInDecibels)
}

@Composable
fun WavetableSynthesizerApp(
  modifier: Modifier,
  synthesizerViewModel: WavetableSynthesizerViewModel = viewModel()
) {
  val synthesizerState = rememberSynthesizerState()

  WavetableSynthesizerTheme {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top,
    ) {
      WavetableSelectionPanel(modifier, synthesizerState.wavetable)
      ControlsPanel(modifier, synthesizerViewModel, synthesizerState.volumeInDecibels)
    }
  }
}

@Composable
private fun ControlsPanel(
  modifier: Modifier,
  synthesizerViewModel: WavetableSynthesizerViewModel,
  volumeInDecibels: MutableState<Float>
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
        .padding(vertical = 40.dp)
    ) {
      VolumeControl(modifier, volumeInDecibels)
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
private fun VolumeControl(modifier: Modifier, volumeInDecibels: MutableState<Float>) {
  Icon(imageVector = Icons.Filled.VolumeUp, contentDescription = null)
  Slider(
    value = volumeInDecibels.value,
    onValueChange = { volumeInDecibels.value = it },
    modifier = modifier.rotate(270f),
    valueRange = (-60f)..0f
  )
  Icon(imageVector = Icons.Filled.VolumeMute, contentDescription = null)
}

@Composable
private fun WavetableSelectionPanel(modifier: Modifier, wavetable: MutableState<Wavetable>) {
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
      WavetableSelectionButtons(modifier, wavetable)
    }
  }
}

@Composable
private fun WavetableSelectionButtons(modifier: Modifier, wavetable: MutableState<Wavetable>) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly
  ) {
    WavetableButton(
      modifier = modifier,
      representedWavetable = Wavetable.SINE,
      wavetable = wavetable
    )
    WavetableButton(
      modifier = modifier,
      representedWavetable = Wavetable.TRIANGLE,
      wavetable = wavetable
    )
    WavetableButton(
      modifier = modifier,
      representedWavetable = Wavetable.SQUARE,
      wavetable = wavetable
    )
    WavetableButton(
      modifier = modifier,
      representedWavetable = Wavetable.SAW,
      wavetable = wavetable
    )
  }
}

@Composable
private fun WavetableButton(
  modifier: Modifier,
  representedWavetable: Wavetable,
  wavetable: MutableState<Wavetable>
) {
  Button(modifier = modifier, onClick = {
    wavetable.value = representedWavetable
  }) {
    Text(representedWavetable.toString())
  }
}

@Preview(showBackground = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 720)
@Composable
fun WavetableSynthesizerPreview() {
  WavetableSynthesizerApp(Modifier, WavetableSynthesizerViewModel(LoggingWavetableSynthesizer()))
}
