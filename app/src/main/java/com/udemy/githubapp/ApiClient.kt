package com.udemy.githubapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private  const val BASE_URI ="https://api.github.com/"

    private  val okHttpClient = OkHttpClient.Builder()
        .addInterceptor{
            val request =  it.request()
                .newBuilder()
                .addHeader("Authorization","Bearer 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")
                .build()
            it.proceed(request)
        }
        .build()
    //retrofit 연동시키기
    val retrofit: Retrofit =  Retrofit.Builder()
        .baseUrl(BASE_URI)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}