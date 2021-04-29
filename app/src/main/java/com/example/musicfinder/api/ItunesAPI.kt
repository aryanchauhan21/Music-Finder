package com.example.musicfinder.api

import com.example.musicfinder.models.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesAPI {

    @GET("search")
    suspend fun searchForQuery(
            @Query("term")
            queryText: String
    ): Response<SearchResponse>

}