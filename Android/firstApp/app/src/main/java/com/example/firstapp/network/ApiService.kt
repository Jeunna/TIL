package com.example.firstapp.network

import com.example.firstapp.data.Account
import com.example.firstapp.data.ContentList
import com.example.firstapp.data.Details
import com.example.firstapp.data.MarsProperty
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("realestate")
    suspend fun getProperties(
        //Query = URL?user_name=userName&,,
        @Query("filter") type: String
    ): List<MarsProperty>

    @GET(Constants.CONTENT_LINK_EXT)
    fun getContents(
        @Header("authorization") authorization: String,
    ): Call<ContentList>

    @GET(Constants.CONTENT_LINK_EXT)
    suspend fun getContentList(
        @Header("authorization") authorization: String,
    ): Response<ContentList>

    @FormUrlEncoded //Content-Type = application/x-www-form-urlencoded
    @POST(Constants.LOGIN_EXT)
    fun login(
        @Header("authorization") authorization: String,
        @Field("user") user: String
    ): Call<Account>

    @GET
    suspend fun getDetail(
        @Header("authorization") authorization: String,
        @Url url: String
    ): Response<Details>
}
