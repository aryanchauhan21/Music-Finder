package com.example.musicfinder.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicfinder.db.SongsDB
import com.example.musicfinder.models.SearchResponse
import com.example.musicfinder.repository.Repository
import com.example.musicfinder.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel(
    app: Application,
    db: SongsDB
): AndroidViewModel(app) {

    val repository = Repository(db)
    val searchedSongs : MutableLiveData<Resource<SearchResponse>> = MutableLiveData()

    fun searchForQuery(queryText: String) {
        searchedSongs.postValue(Resource.Loading())
        viewModelScope.launch {
            if(hasInternetConnection()) {
                val response = repository.searchForQueryOnline(queryText)
                searchedSongs.postValue(processedResponse(response))
            } else{
                val response = repository.searchForQueryOffline("%$queryText%")
                val searchResponse = SearchResponse(response.size, response)
                searchedSongs.postValue(Resource.Success(searchResponse))
            }
        }
    }

    private fun processedResponse(response: Response<SearchResponse>): Resource<SearchResponse> {
        if(response.isSuccessful) {
            viewModelScope.launch {
                // We also add it to database
                response.body()?.results?.let { repository.insertSongs(it) }
            }
            return Resource.Success(response.body())
        } else {
            return Resource.Error(response.message())
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
