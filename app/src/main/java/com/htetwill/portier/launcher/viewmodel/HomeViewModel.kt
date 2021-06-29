package com.htetwill.portier.launcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.htetwill.portier.launcher.model.Config
import com.htetwill.portier.launcher.retrofit.ApiClient
import com.htetwill.portier.launcher.retrofit.ApiService
import com.htetwill.portier.launcher.state.ResultOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel:ViewModel() {

    private val config: MutableLiveData<ResultOf<Config>> = MutableLiveData()

    fun configLiveData() : LiveData<ResultOf<Config>> = config

    fun fetchResponse(url: String) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.fetchPosts()
                if (response.isSuccessful) config.postValue(ResultOf.Success(Config()))
                else config.postValue(ResultOf.Failure("Error: ${response.code()}. Please contact the developer", Exception()))
            } catch (e: HttpException) {
                config.postValue(ResultOf.Failure(e.message, e))
            } catch (e: Throwable) {
                config.postValue(ResultOf.Failure(e.message, e))
            }
        }
    }

    class Factory() : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}