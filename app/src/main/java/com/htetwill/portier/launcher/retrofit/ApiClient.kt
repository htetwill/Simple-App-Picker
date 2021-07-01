package com.htetwill.portier.launcher.retrofit


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://run.mocky.io/"
    private val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(2, TimeUnit.MINUTES)
        .writeTimeout(2, TimeUnit.MINUTES)
        .retryOnConnectionFailure(true)
        .addInterceptor(loggingInterceptor)
        .build()


    fun getClient(): Retrofit {
        when (retrofit) {
            null ->
                retrofit =  Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .client(okHttpClient)
                    .build()
        }
        return retrofit as Retrofit
    }
}