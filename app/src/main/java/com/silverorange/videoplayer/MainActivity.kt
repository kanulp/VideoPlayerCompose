package com.silverorange.videoplayer

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.silverorange.videoplayer.model.VideoModelItem
import com.silverorange.videoplayer.network.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      MaterialTheme() {
        VideoView()
      }
      TopBar()
    }
  }
}
fun provideExoPlayer(context : Context): ExoPlayer {
  val mediaItems: MutableList<MediaItem> = ArrayList()
  var list = arrayListOf<String>()
  list.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")
  list.add("https://d140vvwqovffrf.cloudfront.net/media/5e87b9a811599/full/720.mp4")
  for (i in 0 until list.size) {
    mediaItems.add(MediaItem.fromUri(list[i]))
  }
  val player = ExoPlayer.Builder(context).build()
  player.setMediaItems(mediaItems)
  return player
}

@Composable
fun VideoScreen(data: List<VideoModelItem>?) {

  val playWhenReady by remember { mutableStateOf(true) }
  val context = LocalContext.current
  val playerView = StyledPlayerView(context)
  val player = provideExoPlayer(context = context)
  playerView.player = player

  player.addListener(object : Player.Listener{

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
      super.onMediaItemTransition(mediaItem, reason)
      Log.d("VideoScreen","${mediaItem?.mediaId} | ${mediaItem?.mediaMetadata?.trackNumber} | ${mediaItem?.mediaMetadata?.mediaUri} | $reason")
    }

  })
  LaunchedEffect(player) {
    player.prepare()
    player.playWhenReady = playWhenReady
  }
  AndroidView(factory = {
        playerView }
  )

}

@Composable
fun VideoView(viewModel: VideoListViewModel= hiltViewModel()){
  LaunchedEffect(Unit, block = {
    viewModel.getVideoList()
  })

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row {
            Text("Videos")
          }
        })
    },
    content = {
      var list = viewModel.videoList.observeAsState()
      when(list.value?.status){

        Resource.Status.SUCCESS->{

          Column() {
            VideoScreen(list.value?.data)
            Column(modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())
            ) {
              Text(
                list.value?.data?.get(1)?.title ?: "N/A",
                fontWeight = FontWeight.Bold
              )
              Text(
                list.value?.data?.get(1)?.author?.name?:"N/A",
                fontSize = 18.sp
              )
              Spacer(modifier = Modifier.padding(10.dp))
              Text(
                list.value?.data?.get(1)?.publishedAt?:"N/A",
              )
              Text(
                list.value?.data?.get(1)?.description?:"N/A",
              )
            }
          }
        }
        Resource.Status.ERROR->{
          Text(text = list.value?.message.toString())
        }
        Resource.Status.LOADING->{
          CircularProgressIndicator()
        }
      }
    }
  )
}

@Preview
@Composable
fun TopBar() {
  TopAppBar(title = { Text(text = "Video Player") })
}

@Preview
@Composable
fun TextCard() {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = "Hello world!")
  }
}
