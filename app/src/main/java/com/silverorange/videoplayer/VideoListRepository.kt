package com.silverorange.videoplayer

import com.silverorange.videoplayer.model.VideoModel
import com.silverorange.videoplayer.network.ApiService
import com.silverorange.videoplayer.network.BaseDataSource
import com.silverorange.videoplayer.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import javax.inject.Inject

class VideoListRepository @Inject constructor (val apiService: ApiService) : BaseDataSource() {

    suspend fun getVideoList(): Flow<Resource<VideoModel>> {
        return flow<Resource<VideoModel>> {
            emit(getResult { apiService.getVideosList() })
        }.flowOn(Dispatchers.IO)
    }
}