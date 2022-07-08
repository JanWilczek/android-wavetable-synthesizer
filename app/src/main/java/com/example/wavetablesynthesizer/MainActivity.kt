package com.example.wavetablesynthesizer

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wavetablesynthesizer.ui.theme.WavetableSynthesizerTheme

class MainActivity : ComponentActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    setContent {
      WavetableSynthesizerTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          WavetableSynthesizerApp(Modifier)
        }
      }
    }
  }
}


@Composable
fun WavetableSynthesizerApp(modifier: Modifier) {
  var frequencySliderPosition by remember { mutableStateOf(100f) }
  WavetableSynthesizerTheme {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top,
    ) {
      WavetableSelectionPanel(modifier)
      ControlsPanel(modifier, frequencySliderPosition)
    }
  }
}

@Composable
private fun ControlsPanel(modifier: Modifier, frequencySliderPosition: Float) {
  var frequencySliderPosition1 = frequencySliderPosition
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight(),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(
      modifier = modifier.fillMaxWidth()
    ) {
      Row() {
        PitchControl(modifier, frequencySliderPosition1)
        VolumeControl(modifier)
      }
      Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth().padding(vertical = 40.dp)
      ) {
        PlayControl()
      }
    }
  }
}

@Composable
private fun PlayControl() {
  Button(onClick = {
  }) {
    Text("Play")
  }
}

@Composable
private fun PitchControl(
  modifier: Modifier,
  frequencySliderPosition1: Float
) {
  var frequencySliderPosition11 = frequencySliderPosition1
  Column(
    modifier = modifier.fillMaxWidth(0.7f),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text("Frequency")
    Slider(value = 110f, onValueChange = {
      frequencySliderPosition11 = it
    }, valueRange = 16f..16000f)
    Row(
      horizontalArrangement = Arrangement.Center
    ) {
      Text(text = frequencySliderPosition11.toString())
      Text(" Hz")
    }
  }
}

@Composable
private fun VolumeControl(modifier: Modifier) {
  Column(
    modifier = modifier.fillMaxWidth()
  ) {
//    Icon(imageVector = Icons.Filled.VolumeUp, contentDescription = null)
    Slider(value = 0f, onValueChange = {}, modifier = modifier.rotate(90f))
//    Icon(imageVector = Icons.Filled.VolumeMute, contentDescription = null)
  }
}

@Composable
private fun WavetableSelectionPanel(modifier: Modifier) {
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
      WavetableSelectionButtons(modifier)
    }
  }
}

@Composable
private fun WavetableSelectionButtons(modifier: Modifier) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly
  ) {
    Button(onClick = {
    }) {
      Text("Sine")
    }
    Button(onClick = {
    }) {
      Text("Triangle")
    }
    Button(onClick = {
    }) {
      Text("Square")
    }
    Button(onClick = {
    }) {
      Text("Saw")
    }
  }
}

@Preview(showBackground = true)
@Composable
fun WavetableSynthesizerPreview() {
  WavetableSynthesizerApp(Modifier)
}
