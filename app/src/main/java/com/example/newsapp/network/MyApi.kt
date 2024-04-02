package com.example.newsapp.network
import com.example.newsapp.Constants
import com.example.newsapp.model.NewsFeed
import com.example.newsapp.utils.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MyApi {

    @Headers("Content-Type: application/json")
    @GET("everything")
    suspend fun getNewsFeed(
        @Query("q") q: String? = "tesla",
        @Query("apiKey") apiKey: String? = Constants.API_KEY,
        @Query("page") page: Int? = 1,
        @Query("pageSize") pageSize: Int? = 4
    ): BaseResponse<List<NewsFeed>>

    @GET("top-headlines")
    suspend fun getHeadline(
        @Query("country") country: String? = "us",
        @Query("apiKey") apiKey: String? = Constants.API_KEY,
        @Query("page") page: Int? = 2,
        @Query("pageSize") pageSize: Int? = 1
    ): BaseResponse<List<NewsFeed>>
}