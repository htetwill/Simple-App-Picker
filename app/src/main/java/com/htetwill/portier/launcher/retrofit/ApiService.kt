package com.htetwill.portier.launcher.retrofit

import com.htetwill.portier.launcher.model.Config
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("application/119267/article/get_articles_list")
    suspend fun fetchPosts(): Response<Config>
}