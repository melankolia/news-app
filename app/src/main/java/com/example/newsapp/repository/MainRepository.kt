package com.example.newsapp.repository

import com.example.newsapp.model.NewsFeed
import com.example.newsapp.utils.BaseResponse

interface MainRepository {
    suspend fun getNewsFeed(page: Int): BaseResponse<List<NewsFeed>>
    suspend fun getHeadline(): BaseResponse<List<NewsFeed>>
}