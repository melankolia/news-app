package com.example.newsapp

import com.example.newsapp.network.MyApi
import com.example.newsapp.repository.MainRepository
import com.example.newsapp.repository.MainRepositoryImpl
import com.example.newsapp.ui.screens.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {
    single {
        Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
    }
    single<MainRepository> {
        MainRepositoryImpl(get())
    }
    viewModel {
        MainViewModel(get())
    }
}