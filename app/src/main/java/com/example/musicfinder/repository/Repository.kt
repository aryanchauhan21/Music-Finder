package com.example.musicfinder.repository

import com.example.musicfinder.api.RetrofitInstance
import com.example.musicfinder.db.SongsDB
import com.example.musicfinder.models.SearchResponse
import com.example.musicfinder.models.Song
import retrofit2.Response

class Repository (
    val db: SongsDB
) {
    suspend fun searchForQueryOnline(queryText: String) : Response<SearchResponse> {
        return RetrofitInstance.api.searchForQuery(queryText)
    }

    suspend fun insertSongs(songs: List<Song>) {
        db.songsDao().insertSongs(songs)
    }

    suspend fun searchForQueryOffline(queryText: String) : List<Song> {
        return db.songsDao().getSongsByArtist(queryText)
    }
}