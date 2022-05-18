package com.silverorange.videoplayer.network

import com.silverorange.videoplayer.model.VideoModel
import retrofit2.Response
import retrofit2.http.GET


interface ApiService {

    @GET("videos")
    suspend fun getVideosList(): Response<VideoModel>

}