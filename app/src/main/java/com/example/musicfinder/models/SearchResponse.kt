package com.example.musicfinder.models

data class SearchResponse(
    val resultCount: Int,
    val results: List<Result>
)