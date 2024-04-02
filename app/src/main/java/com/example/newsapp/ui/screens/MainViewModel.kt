package com.example.newsapp.ui.screens

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsFeed
import com.example.newsapp.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {
    private val page = mutableIntStateOf(0)

    private val _color = MutableStateFlow<Int>(0xFFFF)
    val color = _color.asStateFlow()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isLoading2 = MutableLiveData<Boolean>()
    val isLoading2: LiveData<Boolean> get() = _isLoading2

    private val _headNews = MutableLiveData<List<NewsFeed>>()
    val headNews: LiveData<List<NewsFeed>> get() = _headNews

    private val _regularNews = MutableLiveData<List<NewsFeed>>()
    val regularNews: LiveData<List<NewsFeed>> get() = _regularNews

    fun doNetworkCall() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                page.intValue += 1
                _isLoading.postValue(true)
                val response = repository.getNewsFeed(page.intValue)
                response.articles.forEach { news ->
                    val parsedDateTime = LocalDateTime.parse(news.publishedAt, DateTimeFormatter.ISO_DATE_TIME)
                    val formattedDate = parsedDateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH))
                    news.publishedAt = formattedDate
                }
                val currentRegularNews = regularNews.value ?: emptyList()
                _regularNews.postValue(currentRegularNews + response.articles)
            } catch (e: Exception) {
                Log.v("Error", e.message ?: "Unknown error occurred")
            } finally {
                _isLoading.postValue(false) // Use postValue to update LiveData on the main thread
            }
        }
    }

    fun getHeadline() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading2.postValue(true) // Use postValue to update LiveData on the main thread
                val response = repository.getHeadline()
                response.articles.forEach { news ->
                    val parsedDateTime = LocalDateTime.parse(news.publishedAt, DateTimeFormatter.ISO_DATE_TIME)
                    val formattedDate = parsedDateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH))
                    news.publishedAt = formattedDate
                }
                _headNews.postValue(response.articles)
            } catch (e: Exception) {
                Log.v("Error", e.message ?: "Unknown error occurred")
            } finally {
                _isLoading2.postValue(false) // Use postValue to update LiveData on the main thread
            }
        }
    }
}
