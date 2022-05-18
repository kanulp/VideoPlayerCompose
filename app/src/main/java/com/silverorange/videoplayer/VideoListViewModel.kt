package com.silverorange.videoplayer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silverorange.videoplayer.model.VideoModelItem
import com.silverorange.videoplayer.network.ApiService
import com.silverorange.videoplayer.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(val repository: VideoListRepository) : ViewModel() {

    private val _videoList = MutableLiveData<Resource<List<VideoModelItem>>>()
    val videoList: LiveData<Resource<List<VideoModelItem>>>
        get() = _videoList

    fun getVideoList() =
        viewModelScope.launch {

            repository.getVideoList().collect{
                    values->
                _videoList.value =  values
            }
    }
}