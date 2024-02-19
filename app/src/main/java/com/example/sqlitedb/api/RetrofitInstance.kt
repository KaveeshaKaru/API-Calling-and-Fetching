package com.example.sqlitedb.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        val mainURL = "https://api-staging-ez.azurewebsites.net/api/External_Api/Mobile_Api/"

        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(mainURL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
    }

}