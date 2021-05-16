package com.example.firstapp.network

import android.util.Base64
import android.util.Log
import com.example.firstapp.data.Account
import com.example.firstapp.data.ContentList
import com.example.firstapp.data.ContentModel
import com.example.firstapp.network.ContentApi.retrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContentService {
    private val TAG = this::class.java.simpleName

    /**
     * Log-in
     */
    fun logIn(
        email: String, password: String,
        success: (Account) -> Unit,
        error: (Call<Account>, Throwable) -> Unit
    ){
        //retrofit request
        val authorization = encodeTo64("${email}:${password}")
        val call = retrofitService.login(authorization, "")

        call.enqueue(object : Callback<Account> {
            override fun onResponse(
                call: Call<Account>,
                response: Response<Account>
            ) {
                if (response.isSuccessful) {
//                        val data = response.body()?.contentList
                    Log.d(TAG, "${response.body()?.toString()}")

                    val data = response.body() // NULL check with '?'
                    data?.let { success(data) } // if data is not null, go to
                } else {
                    Log.d(TAG, "${response.body()?.toString()}")
                    Log.d(TAG, "not success")
                }
            }

            override fun onFailure(call: Call<Account>, t: Throwable?) {
                Log.d(TAG, "Failure : {$t}")
                call.let {
                    if (t != null) {
                        error(it, t)
                    }
                }
            }
        })
    }

    /**
     * get content list
     */
    fun getContentList(
        success: (List<ContentModel>) -> Unit,
        error: (Call<ContentList>, Throwable) -> Unit
    ) {

        val call = retrofitService.getContents(Constants.API_KEY)

        call.enqueue(object : Callback<ContentList> {

            override fun onResponse(
                call: Call<ContentList>,
                response: Response<ContentList>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.contentList

//                    Log.d(TAG, "Parsed: $data")
                    data?.let { success(data) }
                } else {
                    Log.d(TAG, "${response.body()?.toString()}")
                }
            }

            override fun onFailure(call: Call<ContentList>, t: Throwable?) {
                Log.d(TAG, "Failure : {$t}")
                call.let {
                    if (t != null) {
                        error(it, t)
                    }
                }
            }
        })
    }

    suspend fun getDetails(contentList: List<ContentModel>): List<ContentModel>
    {
        for(i in contentList){
            try {
//                val call = async { }
                val request = retrofitService.getDetail(Constants.API_KEY, i.contentLink)

                if(request.isSuccessful){
                    i.details = request.toString()
                }

            } catch (exception: Exception) {
//                    exception.printStackTrace()
                continue
            }
        }

        return contentList
    }

    // Base64 encode
    private fun encodeTo64(toEncode: String): String {
        val toEncodeAsBytes = toEncode.toByteArray()
        val encodeString = Base64.encodeToString(toEncodeAsBytes, Base64.NO_WRAP)

        return encodeString
    }
}