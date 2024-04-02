package com.example.newsapp.repository

import android.util.Log
import com.example.newsapp.model.NewsFeed
import com.example.newsapp.network.MyApi
import com.example.newsapp.utils.BaseResponse

class MainRepositoryImpl(
    private val api: MyApi
): MainRepository {
    override suspend fun getNewsFeed(page: Int): BaseResponse<List<NewsFeed>> {
        return api.getNewsFeed(page = page)
    }

    override suspend fun getHeadline(): BaseResponse<List<NewsFeed>> {
        return api.getHeadline()
    }
}