package com.silverorange.videoplayer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
      //TextCard()
    }
  }
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
      Log.d("MainActivity","data : ${viewModel.videoList}")
      var list = viewModel.videoList.observeAsState()
      when(list.value?.status){
        Resource.Status.SUCCESS->{
          Column(modifier = Modifier.padding(16.dp)) {


            LazyColumn(modifier = Modifier.fillMaxHeight()) {
              items(viewModel.videoList.value?.data!!) { todo ->
                Column {
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Box(
                      modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 16.dp, 0.dp)
                    ) {
                      Text(
                        todo.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                      )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                  }
                  Text(
                    todo.publishedAt,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Text(
                    todo.fullURL,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Text(
                    todo.description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Divider()
                }
              }
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
