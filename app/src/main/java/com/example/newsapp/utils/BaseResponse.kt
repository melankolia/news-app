package com.example.newsapp.utils

data class BaseResponse <T> (
    val status: String,
    val totalResult: Int,
    val articles: T
)