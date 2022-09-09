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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
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
    // pass the synthesizer to the ViewModel
    synthesizerViewModel.wavetableSynthesizer = synthesizer
    setContent {
      WavetableSynthesizerTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          // pass the ViewModel down the composables' hierarchy
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
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top,
    ) {
      WavetableSelectionPanel(modifier, synthesizerViewModel)
      ControlsPanel(modifier, synthesizerViewModel)
    }
}

@Composable
private fun ControlsPanel(
  modifier: Modifier,
  synthesizerViewModel: WavetableSynthesizerViewModel
) {
  Row(
    modifier = modifier
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
  // The label of the play button is now an observable state, an instance of State<Int?>.
  // State<Int?> is used because the label is the id value of the resource string.
  // Thanks to the fact that the composable observes the label,
  // the composable will be recomposed (redrawn) when the observed state changes.
  val playButtonLabel = synthesizerViewModel.playButtonLabel.observeAsState()

  PlayControlContent(modifier = modifier,
    // onClick handler now simply notifies the ViewModel that it has been clicked
    onClick = {
      synthesizerViewModel.playClicked()
    },
    // playButtonLabel will never be null; if it is, then we have a serious implementation issue
    buttonLabel = stringResource(playButtonLabel.value!!))
}

@Composable
private fun PlayControlContent(modifier: Modifier, onClick: () -> Unit, buttonLabel: String) {
  Button(modifier = modifier,
    onClick = onClick) {
    Text(buttonLabel)
  }
}

@Composable
private fun PitchControl(
  modifier: Modifier,
  synthesizerViewModel: WavetableSynthesizerViewModel
) {
  // if the frequency changes, recompose this composable
  val frequency = synthesizerViewModel.frequency.observeAsState()
  // the slider position state is hoisted by this composable; no need to embed it into
  // the ViewModel, which ideally, shouldn't be aware of the UI.
  // When the slider position changes, this composable will be recomposed as we explained in
  // the UI tutorial.
  val sliderPosition = rememberSaveable {
    mutableStateOf(
      // we use the ViewModel's convenience function to get the initial slider position
      synthesizerViewModel.sliderPositionFromFrequencyInHz(frequency.value!!)
    )
  }

  PitchControlContent(
    modifier = modifier,
    pitchControlLabel = stringResource(R.string.frequency),
    value = sliderPosition.value,
    // on slider position change, update the slider position and the ViewModel
    onValueChange = {
      sliderPosition.value = it
      synthesizerViewModel.setFrequencySliderPosition(it)
    },
    // this range is now [0, 1] because the ViewModel is responsible for calculating the frequency
    // out of the slider position
    valueRange = 0F..1F,
    // this label could be moved into the ViewModel but it doesn't have to be because this
    // composable will anyway be recomposed on a frequency change
    frequencyValueLabel = stringResource(R.string.frequency_value, frequency.value!!)
  )
}

@Composable
private fun PitchControlContent(
  modifier: Modifier,
  pitchControlLabel: String,
  value: Float,
  onValueChange: (Float) -> Unit,
  valueRange: ClosedFloatingPointRange<Float>,
  frequencyValueLabel: String
) {
  Text(pitchControlLabel, modifier = modifier)
  Slider(modifier = modifier, value = value, onValueChange = onValueChange, valueRange = valueRange)
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.Center
  ) {
    Text(modifier = modifier, text = frequencyValueLabel)
  }
}

@Composable
private fun VolumeControl(modifier: Modifier, synthesizerViewModel: WavetableSynthesizerViewModel) {
  // volume value is now an observable state; that means that the composable will be
  // recomposed (redrawn) when the observed state changes.
  val volume = synthesizerViewModel.volume.observeAsState()

  VolumeControlContent(
    modifier = modifier,
    // volume value should never be null; if it is, there's a serious implementation issue
    volume = volume.value!!,
    // use the value range from the ViewModel
    volumeRange = synthesizerViewModel.volumeRange,
    // on volume slider change, just update the ViewModel
    onValueChange = { synthesizerViewModel.setVolume(it) })
}

@Composable
private fun VolumeControlContent(
  modifier: Modifier,
  volume: Float,
  volumeRange: ClosedFloatingPointRange<Float>,
  onValueChange: (Float) -> Unit
) {
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
      value = volume,
      onValueChange = onValueChange,
      modifier = modifier
        .width(sliderHeight.dp)
        .rotate(270f),
      valueRange = volumeRange
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
      Text(stringResource(R.string.wavetable))
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
        // update the ViewModel when the given wavetable is clicked
        onClick = {
          synthesizerViewModel.setWavetable(wavetable)
        },
        // set the label to the resource string that corresponds to the wavetable
        label = stringResource(wavetable.toResourceString()),
      )
    }
  }
}

@Composable
private fun WavetableButton(
  modifier: Modifier,
  onClick: () -> Unit,
  label: String,
) {
  Button(modifier = modifier, onClick = onClick) {
    Text(label)
  }
}

@Preview(showBackground = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 720)
@Composable
fun WavetableSynthesizerPreview() {
  WavetableSynthesizerTheme {
    WavetableSynthesizerApp(Modifier, WavetableSynthesizerViewModel())
  }
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