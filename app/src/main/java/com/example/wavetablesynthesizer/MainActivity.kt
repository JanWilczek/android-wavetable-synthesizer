package com.example.wavetablesynthesizer

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import com.example.wavetablesynthesizer.ui.theme.WavetableSynthesizerTheme

class MainActivity : ComponentActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    setContent {
      WavetableSynthesizerTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          DefaultPreview()
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  var frequencySliderPosition by remember { mutableStateOf(100f) }
  WavetableSynthesizerTheme {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top,
    ) {
      Row(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column() {
          Text("Wavetable")
          Row() {
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
      }
      Row(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(
          modifier = Modifier.fillMaxWidth(0.7f),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text("Frequency")
          Slider(value = 110f, onValueChange = {
            frequencySliderPosition = it
          }, valueRange = 16f..16000f)
          Row(
            horizontalArrangement = Arrangement.Center
          ) {
            Text(text = frequencySliderPosition.toString())
            Text(" Hz")
          }
        }
        Column(
          modifier = Modifier.fillMaxWidth()
        ) {
          Slider(value = 0f, onValueChange = {

          }, modifier = Modifier.rotate(90f))
        }
      }
    }
  }
}