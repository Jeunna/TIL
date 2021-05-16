package com.example.firstapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val BASE_URL = Constants.SERVER_PREFIX + Constants.SERVER_LINK

enum class MarsApiFilter(val value: String) {
    SHOW_RENT("rent"),
    SHOW_BUY("buy"),
    SHOW_ALL("all") }

// Json converter
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// for debug
//private val interceptor = HttpLoggingInterceptor().apply {
//    level = HttpLoggingInterceptor.Level.BODY
//}
private val client: OkHttpClient = OkHttpClient.Builder()
//    .addInterceptor(headerInterceptor)
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .build()

// API request
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
//    .client(client)
//    .addConverterFactory(GsonConverterFactory.create())
//    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

object ContentApi {
    val retrofitService : ApiService by lazy { retrofit.create(ApiService::class.java) }
}