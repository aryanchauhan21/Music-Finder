package com.example.musicfinder.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicfinder.db.SongsDB

class SearchViewModelProviderFactory(
    val app: Application,
    val db: SongsDB
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(app, db) as T
    }

}