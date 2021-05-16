package com.example.firstapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.firstapp.data.ContentModel

class DetailViewModel (content: ContentModel,
                       app: Application) : AndroidViewModel(app) {

    private val _selected = MutableLiveData<ContentModel>()

    val selected: LiveData<ContentModel>
        get() = _selected

    init {
        _selected.value = content
    }
}