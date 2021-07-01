package com.htetwill.portier.launcher.retrofit

import com.htetwill.portier.launcher.model.Config
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
//    v3/23780f2c-de54-47fa-9ca9-5bd3ab03fb50
    @GET
    suspend fun fetchPosts(@Url url : String): Response<Config>
}