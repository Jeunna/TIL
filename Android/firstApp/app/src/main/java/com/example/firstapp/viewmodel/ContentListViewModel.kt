package com.example.firstapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firstapp.data.ContentModel
import com.example.firstapp.network.Constants.Companion.API_KEY
import com.example.firstapp.network.ContentApi
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }

class ContentListViewModel : ViewModel() {

    private val authorization = API_KEY

    private val _status = MutableLiveData<ApiStatus>()

    val status: LiveData<ApiStatus>
        get() = _status

    private val _contents = MutableLiveData<List<ContentModel>>()

    val contents: LiveData<List<ContentModel>>
        get() = _contents

    private val _navigateToSelectedContent = MutableLiveData<ContentModel>()
    val navigateToSelectedContent: LiveData<ContentModel>
        get() = _navigateToSelectedContent


    init {
        getContents()
    }

    private fun getContents() {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _contents.value = ContentApi.retrofitService.getContentList(authorization).body()!!.contentList
                _status.value = ApiStatus.DONE

                getDetail()

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _contents.value = ArrayList()
            }
        }
    }

    private fun getDetail(){
        viewModelScope.launch {
//        CoroutineScope(Dispatchers.IO).launch {
            for(i in _contents.value!!){
                try {
//                val call = async { }
                    val request = ContentApi.retrofitService.getDetail(API_KEY, i.contentLink)

                    if(request.isSuccessful){
                        i.details = request.toString()
                    }

                } catch (exception: Exception) {
//                    exception.printStackTrace()
                    continue
                }
            }
        }
    }

    fun updateContents() {
        getContents()
    }

    fun showContent(content: ContentModel) {
        _navigateToSelectedContent.value = content
    }

    fun showContentComplete() {
        _navigateToSelectedContent.value = null
    }

}
